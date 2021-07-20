package com.wsl.model.pojo.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserAccountParam {

    @NotBlank(message = "id不能为空")
    private String id;

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    private String name;

    /**
     * QQ
     */
    private String qq;

    /**
     * 微信
     */
    private String wx;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String phone;
}
