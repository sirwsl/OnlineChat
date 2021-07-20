package com.wsl.model.converter;

import com.wsl.model.entity.User;
import com.wsl.model.pojo.params.UserAccountParam;
import com.wsl.model.pojo.params.UserParam;
import com.wsl.model.pojo.vos.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 用户转换类
 */
@Mapper
public interface UserConverter {
    UserConverter INSTANCE = Mappers.getMapper(UserConverter.class);


    @Mappings(value = {
            @Mapping(target = "headImg",source = "headImg",defaultValue = "https://static.wslhome.top/OnlineChat/headerImg/xxx.jpg")
    })
    User userParam2User(UserParam userParam);

    @Mappings(value = {
            @Mapping(target = "avatar",source = "headImg"),
            @Mapping(target = "id",source = "id")
    })
    UserVO user2UserVO(User user);
}
