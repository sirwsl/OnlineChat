package com.wsl.model.converter;

import com.wsl.model.entity.Room;
import com.wsl.model.pojo.params.RoomParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 聊天室转换类
 */
@Mapper
public interface RoomConverter {
    RoomConverter INSTANCE = Mappers.getMapper(RoomConverter.class);


    @Mappings(value = {
            @Mapping(target = "headImg",source = "headImg",defaultValue = "https://static.wslhome.top/OnlineChat/headerImg/xxx.jpg")
    })
    Room roomParam2Room(RoomParam userParam);
}
