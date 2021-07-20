package com.wsl.service.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.wsl.model.entity.User;
import com.wsl.model.pojo.params.BaseUserParam;
import com.wsl.model.pojo.params.PasswordParam;
import com.wsl.model.pojo.params.UserAccountParam;
import com.wsl.model.pojo.params.UserParam;
import com.wsl.model.pojo.vos.UserVO;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author user
 * @since 2021-07-02
 */
public interface UserService extends IService<User> {

    /**
     * 添加一个用户
     * @param userParams：用户参数
     * @return 处理结果
     */
    boolean addUser(UserParam userParams);

    /**
     * 更新用户基本信息
     * @param baseUserParam：用户基本信息
     * @return 前端UserVO
     */
    UserVO updateBaseInfo(BaseUserParam baseUserParam, HttpServletResponse response);

    /**
     * 修改当前用户密码
     * @param passwordParam：密码param
     * @return 处理结果
     */
    boolean updatePassword(PasswordParam passwordParam);

    /**
     * 修改绑定账号
     * @param userAccountParam：
     * @return true/false
     */
    boolean updateAccount(UserAccountParam userAccountParam);
}
