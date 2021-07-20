package com.wsl.web.api;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wsl.common.Result;
import com.wsl.common.component.request.AbstractCurrentRequestComponent;
import com.wsl.model.entity.Room;
import com.wsl.model.pojo.params.RoomParam;
import com.wsl.model.pojo.vos.FriendsVO;
import com.wsl.service.service.FriendsRoomService;
import com.wsl.service.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Objects;


@RestController
@RequestMapping("/user")
@Slf4j
public class RoomApi {

    @Resource
    private RoomService roomService;

    @Resource
    private FriendsRoomService friendRoomService;

    @Resource
    private AbstractCurrentRequestComponent abstractCurrentRequestComponent;

    /**
     * 查找聊天室
     *
     * @param name
     * @return
     */
    @GetMapping("/getRoom/v1")
    public Result<Room> getRoom(String name) {
        if (StringUtils.isNotBlank(name)) {
            Room one = roomService.getOne(new QueryWrapper<Room>().eq(Room.NAME, name));
            if (Objects.nonNull(one)) {
                return Result.success(one);
            }
        }
        return Result.error("error", "查找聊天室失败");
    }

    /**
     * 加入聊天室
     *
     * @param json
     * @return
     */
    @PostMapping("/addRoom/v1")
    public Result<FriendsVO> addRoom(@RequestBody JSONObject json){
        if (StringUtils.isBlank(json.get("id").toString())) {
            return Result.error("error", "请输入房间id");
        }
        String id = abstractCurrentRequestComponent.getCurrentUser().getId();
       try {
           return Result.success(friendRoomService.addRoom(json.get("id").toString(), id));
       }catch (Exception e){
           return Result.error("error","添加失败");
       }

    }

    /**
     * 退出聊天室
     *
     * @param roomId
     * @return
     */
    @GetMapping("/rmRoom/v1")
    public Result<String> rmRoom(@RequestParam String roomId) {
        if (StringUtils.isBlank(roomId)) {
            return Result.error("error", "请输入房间id");
        }
        String id = abstractCurrentRequestComponent.getCurrentUser().getId();
        try{
            boolean flag = friendRoomService.rmRoom(roomId, id);
            if (flag) {
                return Result.success("退出聊天室成功");
            }
        }catch (Exception e){
            log.error("退出失败");
        }
        return Result.error("error", "退出聊天室失败，一会再试试");
    }


    /**
     * 创建聊天室
     *
     * @param roomParam
     * @return
     */
    @PostMapping("/createRoom/v1")
    public Result<String> createRoom(@Valid @RequestBody RoomParam roomParam) {
        String id = abstractCurrentRequestComponent.getCurrentUser().getId();
        int count = roomService.count(new QueryWrapper<Room>().eq(Room.NAME, roomParam.getName()));
        if (count>0){
            return Result.error("群账号重复，请重新输入群账号");
        }
        boolean insert = roomService.createRoom(roomParam,id);
        if (insert){
            return Result.success("创建聊天室成功");
        }
        return Result.error("创建聊天室失败");
    }

    /**
     * 删除聊天室
     *
     * @param roomId
     * @return
     */
    @PostMapping("/delRoom/v1")
    public Result<String> deleteRoom(@RequestParam String roomId) {
        if (StringUtils.isNotBlank(roomId)){
            String id = abstractCurrentRequestComponent.getCurrentUser().getId();
            try{
                boolean insert = roomService.deleteRoom(roomId,id);
                if (insert){
                    return Result.success("删除聊天室成功");
                }
            }catch (Exception e){
                log.error("删除成功");
            }
        }

        return Result.error("删除聊天室失败");
    }

}

