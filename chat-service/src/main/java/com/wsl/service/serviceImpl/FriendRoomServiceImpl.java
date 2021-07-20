package com.wsl.service.serviceImpl;


import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wsl.dao.mapper.RoomMapper;
import com.wsl.dao.mapper.UserMapper;
import com.wsl.model.entity.Room;
import com.wsl.model.entity.User;
import com.wsl.model.pojo.bos.ChatUserBO;
import com.wsl.model.pojo.vos.FriendsVO;
import com.wsl.service.service.FriendsRoomService;
import com.wsl.dao.mapper.FriendRoomMapper;
import com.wsl.model.entity.FriendRoom;
import com.wsl.service.service.RoomService;
import com.wsl.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.BaseStream;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author user
 * @since 2021-07-02
 */
@Service
public class FriendRoomServiceImpl extends ServiceImpl<FriendRoomMapper, FriendRoom> implements FriendsRoomService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private RoomMapper roomMapper;

    @Resource
    private RoomService roomService;

    @Override
    @Cached(name = "user:friendList", expire = 5, cacheType = CacheType.REMOTE, timeUnit = TimeUnit.MINUTES, localLimit = 40)
    public FriendsVO getFriends(String id) {
        FriendRoom one = getOne(new QueryWrapper<FriendRoom>().eq(FriendRoom.USER_ID, id));
        if (Objects.isNull(one)) {
            return new FriendsVO();
        }
        return getFriendsVO(one);
    }

    @Override
    public FriendsVO cancelFriends(String nowId, String id) {
        FriendRoom one = getOne(new QueryWrapper<FriendRoom>().eq(FriendRoom.USER_ID, nowId));
        if (Objects.isNull(one)) {
            return new FriendsVO();
        }
        List<String> collect = Arrays.stream(one.getFriendIds().split(",")).collect(Collectors.toList());
        collect.remove(id);
        String MyCare = CommonUtil.covertStringList2Str(collect);
        one.setFriendIds(MyCare);
        update(new UpdateWrapper<FriendRoom>().set(FriendRoom.FRIENDS_IDS, MyCare).eq(FriendRoom.USER_ID, nowId));
        return getFriendsVO(one);
    }

    @Override
    public FriendsVO addFriend(String nowId, String id) {
        FriendRoom one = getOne(new QueryWrapper<FriendRoom>().eq(FriendRoom.USER_ID, nowId));
        List<String> collect = new ArrayList<>(128);
        if (Objects.isNull(one)) {
            baseMapper.insert(new FriendRoom().setUserId(nowId));
        }else{
            if (StringUtils.isNotEmpty(one.getFriendIds())){
                collect.addAll(Arrays.stream(one.getFriendIds().split(",")).collect(Collectors.toList()));
            }
        }
        collect.add(id);
        String careMe = CommonUtil.covertStringList2Str(collect);
        one.setFriendIds(careMe);
        update(new UpdateWrapper<FriendRoom>().set(FriendRoom.FRIENDS_IDS, careMe).eq(FriendRoom.USER_ID, nowId));
        return getFriendsVO(one);
    }

    @Override
    public FriendsVO addFriends(String nowId, String name) {
        String id = userMapper.selectList(new QueryWrapper<User>().eq(User.NAME, name)).get(0).getId();
        FriendRoom one = getOne(new QueryWrapper<FriendRoom>().eq(FriendRoom.USER_ID, nowId));
        List<String> collect = new ArrayList<>(128);
        if (Objects.isNull(one)) {
            baseMapper.insert(new FriendRoom().setUserId(nowId));
        }else{
            if (StringUtils.isNotEmpty(one.getFriendIds())){
                collect.addAll(Arrays.stream(one.getFriendIds().split(",")).collect(Collectors.toList()));
            }
        }
        collect.add(id);
        String careMe = CommonUtil.covertStringList2Str( collect);
        one.setFriendIds(careMe);
        update(new UpdateWrapper<FriendRoom>().set(FriendRoom.FRIENDS_IDS, careMe).eq(FriendRoom.USER_ID, nowId));
        return getFriendsVO(one);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FriendsVO addRoom(String roomId, String id) throws Exception {
         FriendRoom friendRoom = getOne(
                new QueryWrapper<FriendRoom>()
                        .eq(FriendRoom.USER_ID, id));
        List<String> collects = new ArrayList<>(128);
         if (Objects.isNull(friendRoom)){
             baseMapper.insert(new FriendRoom().setUserId(id));
         }else{
             if (StringUtils.isNotEmpty(friendRoom.getRoomIds())) {
                 List<String> collect = Arrays.asList(friendRoom.getRoomIds()
                         .split(",")).stream()
                         .collect(Collectors.toList());
                 collects.addAll(collect);
             }
         }
        collects.add(roomId);
        String s = CommonUtil.covertStringList2Str(collects);
        boolean update1 = update(new UpdateWrapper<FriendRoom>().eq(FriendRoom.USER_ID, id).set(FriendRoom.ROOM_IDS, s));
        String userIds = roomMapper.selectById(roomId).getUserIds()+","+id;
        boolean update = roomService.update(new UpdateWrapper<Room>().eq(Room.ID, roomId).set(Room.USER_IDS, userIds));
        if (update&&update1){
            return getFriendsVO(friendRoom);
        }else {
            throw new Exception();
        }
    }


    @Override
    public boolean rmRoom(String roomId, String id) throws Exception {
        List<String> collect = Arrays.stream(getOne(
                new QueryWrapper<FriendRoom>()
                        .eq(FriendRoom.USER_ID, id))
                .getRoomIds()
                .split(","))
                .collect(Collectors.toList());
        collect.remove(roomId);
        String s = CommonUtil.covertStringList2Str(collect);
        Room byId = roomService.getById(roomId);
        boolean flag = true;
        if (id.equals(byId.getUserIds())){
            flag = roomMapper.deleteById(id)>0;
        }else{
            List<String> collect1 = Arrays.stream(byId.getUserIds().split(",")).collect(Collectors.toList());
            collect1.remove(id);
            flag = roomService.update(new UpdateWrapper<Room>().eq(Room.ID,roomId).set(Room.USER_IDS,CommonUtil.covertIdList2Str(collect1)));
        }

        boolean update = update(new UpdateWrapper<FriendRoom>().eq(FriendRoom.USER_ID, id).set(FriendRoom.ROOM_IDS, s));
        if (flag && update) {
            return true;
        } else {
            throw new Exception();
        }
    }

    @Override
    @Cached(name = "user：chatIds", expire = 5, cacheType = CacheType.REMOTE, timeUnit = TimeUnit.MINUTES, localLimit = 40)
    public List<String> getUserIdForChat(String id) {
        Room room = roomService.getById(id);
        if (Objects.nonNull(room)){
            return Arrays.stream(room.getUserIds().split(",")).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    @Cached(name = "user：room", expire = 5, cacheType = CacheType.REMOTE, timeUnit = TimeUnit.MINUTES, localLimit = 40)
    public ChatUserBO getChatUserBO(String id) {
        Room room = roomService.getById(id);
        ChatUserBO chatUserBO = new ChatUserBO();
        if (Objects.nonNull(room)){
            chatUserBO.setAvatar(room.getHeadImg())
                    .setId(id)
                    .setMessage(room.getDetail())
                    .setNickname(room.getNickName())
                    .setType("user")
                    .setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd hh:mm")));
        }
        return chatUserBO;
    }

    private FriendsVO getFriendsVO(FriendRoom friendRoom) {
        if (Objects.isNull(friendRoom)) {
            return new FriendsVO();
        }
        FriendsVO friendsVO = new FriendsVO();
        if (StringUtils.isNotEmpty(friendRoom.getFriendIds())){
            List<String> careMe = Arrays.stream(friendRoom.getFriendIds().split(",")).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(careMe)) {
                List<User> friends = userMapper.selectBatchIds(careMe);
                List<FriendsVO.Friend> friendList = friends.stream()
                        .map(li -> {
                            FriendsVO.Friend temp = new FriendsVO.Friend();
                            temp.setAvatar(li.getHeadImg())
                                    .setId(li.getId())
                                    .setName(li.getName())
                                    .setNickName(li.getNickName());
                            return temp;
                        }).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(friendList)) {
                    friendsVO.setMyFriends(friendList);
                }
            }
        }
        if (StringUtils.isNotEmpty(friendRoom.getRoomIds())){
            List<String> roomId = Arrays.stream(friendRoom.getRoomIds().split(",")).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(roomId)) {
                List<Room> chatRooms = roomMapper.selectBatchIds(roomId);
                List<FriendsVO.Friend> chatRoomList = chatRooms.stream()
                        .map(li -> {
                            FriendsVO.Friend temp = new FriendsVO.Friend();
                            temp.setAvatar(li.getHeadImg())
                                    .setId(li.getId())
                                    .setName(li.getName())
                                    .setNickName(li.getNickName());
                            return temp;
                        }).collect(Collectors.toList());

                if (CollectionUtils.isNotEmpty(chatRoomList)) {
                    friendsVO.setRoom(chatRoomList);
                }
            }
        }
        return friendsVO;
    }
}
