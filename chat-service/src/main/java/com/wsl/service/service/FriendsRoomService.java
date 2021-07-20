package com.wsl.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wsl.model.entity.FriendRoom;
import com.wsl.model.pojo.bos.ChatUserBO;
import com.wsl.model.pojo.vos.FriendsVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author user
 * @since 2021-07-02
 */
public interface FriendsRoomService extends IService<FriendRoom> {

    /**
     * 根据id获取用户好友聊天室
     * @param id
     * @return FriendsVO
     */
    FriendsVO getFriends(String id);

    /**
     * 取消好友
     * @param nowId：用户id
     * @param id：取消id
     * @return FriendsVO
     */
    FriendsVO cancelFriends(String nowId, String id);

    /**
     * 添加好友
     * @param nowId：用户id
     * @param id：添加id
     * @return FriendsVO
     */
    FriendsVO addFriend(String nowId, String id);

    /**
     * 添加好友
     * @param nowId：
     * @param name:对方账号
     * @return
     */
    FriendsVO addFriends(String nowId, String name);

    /**
     * 加入聊天室
     * @param roomId
     * @param id
     * @return
     */
    FriendsVO addRoom(String roomId, String id) throws Exception;

    /**
     * 退出聊天室
     * @param roomId
     * @param id
     * @return
     */
    boolean rmRoom(String roomId, String id) throws Exception;

    /**
     * 根据群聊id获取群聊人员id
     * @param id
     * @return
     */
    List<String> getUserIdForChat(String id);

    /**
     * 根据群聊id获取群信息
     * @param id
     * @return
     */
    ChatUserBO getChatUserBO(String id);
}
