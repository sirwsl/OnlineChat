package com.wsl.service.serviceImpl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsl.common.component.jwt.JwtComponent;
import com.wsl.common.component.jwt.JwtEnum;
import com.wsl.common.component.request.AbstractCurrentRequestComponent;
import com.wsl.dao.mapper.FriendRoomMapper;
import com.wsl.dao.mapper.RoomMapper;
import com.wsl.model.constant.RedisEnum;
import com.wsl.model.converter.UserConverter;
import com.wsl.model.entity.FriendRoom;
import com.wsl.model.entity.Room;
import com.wsl.model.pojo.bos.UserBO;
import com.wsl.model.pojo.params.BaseUserParam;
import com.wsl.model.pojo.params.PasswordParam;
import com.wsl.model.pojo.params.UserAccountParam;
import com.wsl.model.pojo.params.UserParam;
import com.wsl.model.pojo.vos.UserVO;
import com.wsl.service.service.FriendsRoomService;
import com.wsl.service.service.RoomService;
import com.wsl.service.service.UserService;
import com.wsl.dao.mapper.UserMapper;
import com.wsl.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author user
 * @since 2021-07-02
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Value("${jwt.redisToken}")
    private Long redisToken;

    @Value("${req.doMainUrl}")
    private String doMainUrl;


    @Resource
    private AbstractCurrentRequestComponent abstractCurrentRequestComponent;

    @Resource
    private JwtComponent jwtComponent;

    @Resource
    private RedisTemplate<String,UserBO> redisTemplate;

    @Resource
    private FriendRoomMapper friendRoomMapper;

    @Resource
    private RoomService roomService;

    @Override
    public boolean addUser(UserParam userParams) {
        User user = UserConverter.INSTANCE.userParam2User(userParams);
        baseMapper.insert(user);
        try{
            List<Room> admin = roomService.list(new QueryWrapper<Room>().eq(Room.LEADER, "admin"));
            if (CollectionUtils.isNotEmpty(admin)){
                String adminRoomIds = admin.stream().map(Room::getId).collect(Collectors.joining(","));
                FriendRoom friendRoom = new FriendRoom().setUserId(user.getId()).setRoomIds(adminRoomIds);
                friendRoomMapper.insert(friendRoom);
                admin.forEach(li ->li.setUserIds(li.getUserIds()+","+user.getId()));
                roomService.updateBatchById(admin);
            }
        }catch (Exception e){
            log.warn("注册账号，加入聊天室失败");
        }

        return true;
    }

    @Override
    public UserVO updateBaseInfo(BaseUserParam baseUserParam, HttpServletResponse response) {
        String id = abstractCurrentRequestComponent.getCurrentUser().getId();
        update(new UpdateWrapper<User>()
                .eq(User.ID, id)
                .set(User.NICK_NAME, baseUserParam.getNickName())
                .set(User.HEAD_IMG, baseUserParam.getHeadImg())
                .set(User.SEX, baseUserParam.getSex()));
        User user = getById(id);
        UserVO userVO = UserConverter.INSTANCE.user2UserVO(user);
        UserBO userBO = new UserBO(userVO.getId(),userVO.getName(),userVO.getNickName(),userVO.getAvatar(),10);
        String token = jwtComponent.getToken(userBO);
        redisTemplate.opsForValue().set(RedisEnum.USER_VERIFY+userBO.getId(),userBO,redisToken, TimeUnit.SECONDS);
        userVO.setAuthorization(JwtEnum.TOKEN_PREFIX+token);
        response.setHeader(JwtEnum.AUTH_HEADER_KEY, JwtEnum.TOKEN_PREFIX+token);
        response.setHeader(JwtEnum.X_AUTH_TOKEN, JwtEnum.TOKEN_PREFIX+token);
        Cookie cookie = new Cookie("token", JwtEnum.TOKEN_PREFIX + token);
        cookie.setMaxAge(redisToken.intValue());
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        cookie.setDomain(doMainUrl);
        response.addCookie(cookie);
        return userVO;
    }

    @Override
    public boolean updatePassword(PasswordParam passwordParam) {
        String id = abstractCurrentRequestComponent.getCurrentUser().getId();
        String old = getById(id).getPassword();
        if (passwordParam.getOldPassword().equals(old)){
            return update(new UpdateWrapper<User>().set(User.PASSWORD,passwordParam.getNewPassword())
                    .eq(User.ID,id));
        }
        return false;
    }

    @Override
    public boolean updateAccount(UserAccountParam userAccountParam) {
        return update(new UpdateWrapper<User>().eq(User.ID,abstractCurrentRequestComponent.getCurrentUser().getId())
                .set(User.PHONE,userAccountParam.getPhone())
                .set(User.QQ,userAccountParam.getQq())
                .set(User.EMAIL,userAccountParam.getEmail())
                .set(User.WX,userAccountParam.getWx()));
    }
}
