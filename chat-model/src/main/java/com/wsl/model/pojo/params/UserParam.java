package com.wsl.model.pojo.params;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 用户param
 * </p>
 *
 * @author user
 * @since 2021-07-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserParam {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 验证码
     */
    private String code;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空")
    private String name;

    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickName;

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

    /**
     * 性别
     */
    private Boolean sex;

}
