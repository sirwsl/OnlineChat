package com.wsl.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wsl.common.component.jwt.JwtComponent;
import com.wsl.common.component.jwt.JwtEnum;
import com.wsl.common.log.MyLog;
import com.wsl.model.converter.UserConverter;
import com.wsl.model.entity.User;
import com.wsl.model.pojo.bos.ChatUserBO;
import com.wsl.model.pojo.bos.UserBO;
import com.wsl.model.constant.LoggerEnum;
import com.wsl.model.constant.RedisEnum;
import com.wsl.dao.mapper.UserMapper;
import com.wsl.model.pojo.params.LoginParam;
import com.wsl.model.pojo.vos.UserVO;
import com.wsl.service.service.LoginService;
import com.wsl.util.RegexUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author : WangShiLei
 * @date : 2020/11/23 11:00 下午
 **/
@Service
@Slf4j
public class LoginServiceImpl implements LoginService {

    @Value("${jwt.redisToken}")
    private Long redisToken;

    @Value("${req.doMainUrl}")
    private String doMainUrl;

    @Resource
    private UserMapper userMapper;

    @Resource
    private JwtComponent jwtComponent;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @MyLog(detail = "用户登录",grade = LoggerEnum.NONE)
    public UserVO login(HttpServletResponse response, LoginParam loginParam) throws UnsupportedEncodingException {

        UserVO userVO;
        if(loginParam.getType().equals("account")){
            //查看账号是不是手机号
            if(RegexUtils.checkMobile(loginParam.getName())){
                loginParam.setPhone(loginParam.getName());
            }
            //登录
            userVO = userMapper.login(loginParam);
        }else{
            stringRedisTemplate.opsForValue().get(RedisEnum.PHONE_CODE+loginParam.getPhone());
            User user = userMapper.selectOne(new QueryWrapper<User>().eq(User.PHONE, loginParam.getPhone()));
            userVO = UserConverter.INSTANCE.user2UserVO(user);
        }

        if (Objects.isNull(userVO)){
            return null;
        }
        UserBO userBO = new UserBO(userVO.getId(),userVO.getName(),userVO.getNickName(),userVO.getAvatar(),10);
        String token = jwtComponent.getToken(userBO);
        setRes(response,token);
        redisTemplate.opsForValue().set(RedisEnum.USER_VERIFY+userBO.getId(),userBO,redisToken, TimeUnit.SECONDS);
        ChatUserBO chatUserBO = new ChatUserBO("user",userVO.getId(),userVO.getAvatar(),userVO.getName(),null,null);
        redisTemplate.opsForValue().set(RedisEnum.USER_CHAT_BO+chatUserBO.getId(),chatUserBO);
        userVO.setAuthorization(JwtEnum.TOKEN_PREFIX+token);
        return userVO;
    }

    @Override
    @MyLog(detail = "退出登录",grade = LoggerEnum.NONE)
    public boolean exit(HttpServletResponse response, HttpServletRequest request) {
        Cookie token1 = new Cookie("token",null);
        token1.setPath("/");
        token1.setMaxAge(0);
        token1.setDomain(doMainUrl);
        response.addCookie(token1);
        String header = request.getHeader(JwtEnum.AUTH_HEADER_KEY);
        response.setHeader(JwtEnum.AUTH_HEADER_KEY,"");

        if (StringUtils.isEmpty(header)) {
            if (request.getCookies()!=null&&request.getCookies().length > 0){
                for (Cookie cookie : request.getCookies()) {
                    if (JwtEnum.TOKEN.equals(cookie.getName())){
                        header = cookie.getValue();
                        String token = header.substring(JwtEnum.TOKEN_PREFIX.length());
                        String id = jwtComponent.getTokenInfo(token).getId();
                        redisTemplate.delete(RedisEnum.USER_VERIFY+id);

                    }
                }
            }
        }
        return true;

    }



    private void setRes(HttpServletResponse response,String token) throws UnsupportedEncodingException {
        response.setHeader(JwtEnum.AUTH_HEADER_KEY, JwtEnum.TOKEN_PREFIX+token);
        Cookie token1 = new Cookie("token", JwtEnum.TOKEN_PREFIX + token);
        token1.setMaxAge(redisToken.intValue());
        token1.setPath("/");
        token1.setMaxAge(3600);
        token1.setDomain(doMainUrl);
        response.addCookie(token1);
    }
}
