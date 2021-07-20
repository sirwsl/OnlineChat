package com.wsl.model.pojo.vos;


import lombok.Data;

@Data
public class UserVO {
    /**
     * token
     */
    private String authorization;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 账号
     */
    private String name;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 用户id
     */
    private String id;
    /**
     * 权限
     */
    private String authority;
}
