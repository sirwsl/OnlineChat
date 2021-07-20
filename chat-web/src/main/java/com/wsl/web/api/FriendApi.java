package com.wsl.web.api;

import com.alibaba.fastjson.JSONObject;
import com.wsl.common.Result;
import com.wsl.common.component.request.AbstractCurrentRequestComponent;
import com.wsl.model.pojo.vos.FriendsVO;
import com.wsl.service.service.FriendsRoomService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class FriendApi {

    @Resource
    private AbstractCurrentRequestComponent abstractCurrentRequestComponent;

    @Resource
    private FriendsRoomService friendsService;

    /**
     * 获取当前登陆人好友以及聊天室
     * @return FriendsVO
     */
    @GetMapping("/myFriends/v1")
    public Result<FriendsVO> getFriends(){
        String id = abstractCurrentRequestComponent.getCurrentUser().getId();

        FriendsVO friendsVO =  friendsService.getFriends(id);

        return Result.success(friendsVO);
    }

    /**
     * 添加好友
     * @param name :账号
     * @return
     */
    @PostMapping("/addFriends/v1")
    public Result<FriendsVO> addFriends(String name){
        String nowId = abstractCurrentRequestComponent.getCurrentUser().getId();
        FriendsVO friendsVO =  friendsService.addFriends(nowId,name);
        return Result.success(friendsVO);
    }

    /**
     * 添加好友
     * @param jsonObject :
     * @return
     */
    @PostMapping("/addFriendById/v1")
    public Result<FriendsVO> addFriend(@RequestBody JSONObject jsonObject){
        String nowId = abstractCurrentRequestComponent.getCurrentUser().getId();
        FriendsVO friendsVO =  friendsService.addFriend(nowId,jsonObject.get("id").toString());
        return Result.success(friendsVO);
    }

    /**
     * 取消好友
     * @return VO
     * @param id ：
     */
    @GetMapping("/cancelFriends/v1")
    public Result<FriendsVO> cancelMyCare(@RequestParam String id){
        String nowId = abstractCurrentRequestComponent.getCurrentUser().getId();
        FriendsVO friendsVO =  friendsService.cancelFriends(nowId,id);
        return Result.success(friendsVO);
    }
}
