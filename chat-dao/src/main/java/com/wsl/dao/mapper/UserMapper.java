package com.wsl.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wsl.model.entity.User;
import com.wsl.model.pojo.params.LoginParam;
import com.wsl.model.pojo.vos.UserVO;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author user
 * @since 2021-07-02
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 登录
     * @param loginParam：登录参数
     * @return UserVO
     */
    UserVO login(LoginParam loginParam);

}
