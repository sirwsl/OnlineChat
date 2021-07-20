package com.wsl.service.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wsl.model.entity.Room;
import com.wsl.model.pojo.params.RoomParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author user
 * @since 2021-07-02
 */
public interface RoomService extends IService<Room> {

    /**
     * 创建聊天室
     * @param roomParam
     * @param id
     * @return
     */
    boolean createRoom(RoomParam roomParam, String id);

    /**
     * 删除聊天室
     * @param roomId
     * @param id
     * @return
     */
    boolean deleteRoom(String roomId, String id) throws Exception;
}
