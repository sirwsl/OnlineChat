package com.wsl.web.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wsl.common.Result;
import com.wsl.common.component.request.AbstractCurrentRequestComponent;
import com.wsl.model.entity.FriendRoom;
import com.wsl.model.entity.Room;
import com.wsl.model.entity.User;
import com.wsl.model.pojo.params.BaseUserParam;
import com.wsl.model.pojo.params.PasswordParam;
import com.wsl.model.pojo.params.UserAccountParam;
import com.wsl.model.pojo.vos.UserVO;
import com.wsl.service.service.FriendsRoomService;
import com.wsl.service.service.RoomService;
import com.wsl.service.service.UserService;
import com.wsl.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserApi {

    @Resource
    private AbstractCurrentRequestComponent abstractCurrentRequestComponent;

    @Resource
    private UserService userService;

    @Resource
    private FriendsRoomService friendsRoomService;

    @Resource
    private RoomService roomService;


    /**
     * 根据账号检索好友
     * @param name
     * @return
     */
    @GetMapping("/getUser/v1")
    public Result<User> getUser(String name){
        User one = userService.getOne(new QueryWrapper<User>().eq(User.NAME, name));
        if (Objects.nonNull(one)){
            return Result.success(one);
        }
        return Result.error("error","检索失败，该用户不存在");
    }

    /**
     * 获取当前登陆人信息
     * @return
     */
    @GetMapping("/getInfo/v1")
    public Result<User> getUserInfo(){
        String id = abstractCurrentRequestComponent.getCurrentUser().getId();
        User user = userService.getById(id);
        if (Objects.nonNull(user)){
            return Result.success(user);
        }
        return Result.error();
    }

    /**
     * 更新用户基本信息
     * @param baseUserParam：入参
     * @return 更新后信息VO
     */
    @PostMapping("/updateBaseInfo/v1")
    public Result<UserVO> updateBaseInfo(@Valid @RequestBody BaseUserParam baseUserParam, HttpServletResponse response){
        if (abstractCurrentRequestComponent.getCurrentUser().getId().equals(baseUserParam.getId())) {
            UserVO userVO = userService.updateBaseInfo(baseUserParam,response);
            if (Objects.nonNull(userVO)){
                return Result.success(userVO);
            }
        }
        return Result.error("error","更新失败");
    }

    /**
     * 修改密码
     * @param passwordParam
     * @return
     */
    @PostMapping("/updatePassword/v1")
    public Result<String> updatePassword(@Valid @RequestBody PasswordParam passwordParam){
        if (abstractCurrentRequestComponent.getCurrentUser().getId().equals(passwordParam.getId())){
            if (userService.updatePassword(passwordParam)){
                return Result.success("修改密码成功");
            }

        }
        return Result.error("error","修改失败");
    }

    /**
     * 更新账号
     * @param userAccountParam：账号param
     * @return 结果
     */
    @PostMapping("/updateAccount/v1")
    public Result<String> updateAccount(@Valid @RequestBody UserAccountParam userAccountParam){
        if (abstractCurrentRequestComponent.getCurrentUser().getId().equals(userAccountParam.getId())){
            if (userService.updateAccount(userAccountParam)){
                return Result.success("修改成功");
            }
        }
        return Result.error("error","修改失败");
    }

    /**
     * 用户注销
     * @param code：账号
     * @return 处理结果
     */
    @GetMapping("/delUser/{userId}")
    public Result<String> delUser(@PathVariable("userId") String code){
        System.err.println(code);
        if (abstractCurrentRequestComponent.getCurrentUser().getId().equals(code)) {
            boolean b = userService.removeById(abstractCurrentRequestComponent.getCurrentUser().getId());
            try{
                FriendRoom one = friendsRoomService.getOne(new QueryWrapper<FriendRoom>().eq(FriendRoom.USER_ID, code));
                friendsRoomService.removeById(one.getId());
                if (StringUtils.isNotEmpty(one.getRoomIds())){
                    List<String> collect = Arrays.stream(one.getRoomIds().split(",")).collect(Collectors.toList());
                    List<Room> list = roomService.list(new QueryWrapper<Room>().in(Room.ID, collect));
                    list.forEach(li->{
                        List<String> userIds = Arrays.stream(li.getUserIds().split(",")).collect(Collectors.toList());
                        userIds.remove(code);
                        roomService.update(new UpdateWrapper<Room>().eq(Room.ID,li.getId()).set(Room.USER_IDS,CommonUtil.covertStringList2Str(userIds)));
                        roomService.remove(new QueryWrapper<Room>().eq(Room.LEADER,code));
                    });

                }
            }catch (Exception e){
                log.warn("用户注销，清除群聊失败");
            }
            if (b){
                return Result.success("注销成功,感谢您的使用");
            }
        }
        return Result.error("error","注销失败");
    }
}
