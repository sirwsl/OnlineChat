package com.wsl.model.pojo.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class PasswordParam {

    @NotBlank(message = "id不能为空")
    private String id;

    @NotBlank(message = "原密码不能为空")
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    private String newPassword;

    @NotBlank(message = "账号不能为空")
    private String name;
}
