package com.wsl.web.config.cors;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.esotericsoftware.minlog.Log;
import com.wsl.common.component.jwt.JwtComponent;
import com.wsl.common.component.jwt.JwtEnum;
import com.wsl.model.constant.RedisEnum;
import com.wsl.model.pojo.bos.UserBO;

import com.wsl.service.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author WangShilei
 * @date 2020/11/23-13:58
 **/
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private RedisTemplate<String, UserBO> redisTemplate;

    @Resource
    private LoginService loginService;

    @Resource
    private JwtComponent jwtComponent;

    @Value("${jwt.newToken}")
    private Long newToken;

    @Value("${jwt.redisToken}")
    private Long redisToken;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        //获取请求头（如果有此请求头，表示token已经签发）
        String header = request.getHeader(JwtEnum.AUTH_HEADER_KEY);
        if (StringUtils.isEmpty(header)) {
            if (request.getCookies()!=null && CollectionUtils.isNotEmpty(Arrays.asList(request.getCookies()))) {
                for (Cookie cookie : request.getCookies()) {
                    if (JwtEnum.TOKEN.equals(cookie.getName())){
                        header = cookie.getValue();
                    }
                }
            }

            if (StringUtils.isEmpty(header)){
                loginService.exit(response,request);
                //response.sendError(401,"未登录");
                return true;
            }

        }
        //解析请求头（防止伪造token，token内容以"onlineChat "作为开头）
        if (!header.startsWith(JwtEnum.TOKEN_PREFIX)) {
            loginService.exit(response,request);
            response.sendError(401,"Token无效,请重新登录");
            return false;
        }

        String token = header.substring(JwtEnum.TOKEN_PREFIX.length());
        try {
            if (jwtComponent.getTokenTime(token)<1){
                response.sendError(401,"由于您长时间为登录，已退出,请重新登录");
            }
            if (jwtComponent.getTokenTime(token) < newToken) {
                UserBO userBO = redisTemplate.opsForValue().get(RedisEnum.USER_VERIFY + jwtComponent.getTokenInfo(token).getId());
                if (Objects.isNull(userBO)) {
                    loginService.exit(response,request);
                    response.sendError(401,"Token解析异常,请重新登录");
                }
                String newToken = jwtComponent.getToken(userBO);
                response.setHeader(JwtEnum.AUTH_HEADER_KEY, JwtEnum.TOKEN_PREFIX + newToken);
                redisTemplate.opsForValue().set(RedisEnum.USER_VERIFY + userBO.getId(),userBO,redisToken, TimeUnit.SECONDS);
            }
        } catch (RuntimeException e) {
            Log.info("token令牌无效");
            loginService.exit(response,request);
            response.sendError(401,"Token无效,请重新登录");
            return false;
        }

        return true;

    }


}
