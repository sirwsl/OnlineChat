package com.wsl.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wsl.dao.mapper.RoomMapper;
import com.wsl.model.converter.RoomConverter;
import com.wsl.model.entity.FriendRoom;
import com.wsl.model.entity.Room;
import com.wsl.model.pojo.params.RoomParam;
import com.wsl.service.service.FriendsRoomService;
import com.wsl.service.service.RoomService;
import com.wsl.util.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author user
 * @since 2021-07-02
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    @Resource
    private FriendsRoomService friendsRoomService;

    @Resource
    private RoomMapper roomMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createRoom(RoomParam roomParam, String id) {
        roomParam.setLeader(id);
        Room room = RoomConverter.INSTANCE.roomParam2Room(roomParam);
        int flag = roomMapper.insert(room);
        List<String> roomIdsList = new ArrayList<>(128);
        roomIdsList.add(room.getId());
        String roomIds = friendsRoomService.getOne(new QueryWrapper<FriendRoom>().eq(FriendRoom.USER_ID, id)).getRoomIds();
        roomIdsList.addAll(Arrays.asList(roomIds.split(",")));
        boolean update = friendsRoomService.update(new UpdateWrapper<FriendRoom>()
                .set(FriendRoom.ROOM_IDS, CommonUtil.covertStringList2Str(roomIdsList))
                .eq(FriendRoom.USER_ID, id));

        if (flag>0&&update){
            return true;
        }else{
            new Exception();
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRoom(String roomId, String id) throws Exception {
        Map<String,Object> wrapper = new HashMap<>(4);
        wrapper.put(FriendRoom.ID,roomId);
        wrapper.put(FriendRoom.USER_ID,id);
        int delI = roomMapper.deleteByMap(wrapper);
        String roomIds = friendsRoomService.getOne(new QueryWrapper<FriendRoom>().eq(FriendRoom.USER_ID, id)).getRoomIds();
        List<String> roomIdsList = Arrays.asList(roomIds.split(","));
        roomIdsList.remove(roomId);
        boolean update = friendsRoomService.update(new UpdateWrapper<FriendRoom>()
                .set(FriendRoom.ROOM_IDS, CommonUtil.covertStringList2Str(roomIdsList))
                .eq(FriendRoom.USER_ID, id));
        if (delI>0&&update){
            return true;
        }else{
            throw new Exception();
        }
    }
}
