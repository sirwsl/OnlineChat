package com.wsl.model.pojo.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginParam {

    private String code;

    private String name;

    private String phone;

    private String password;

    @NotBlank(message = "类型不能为空")
    private String type;

    private String phoneCode;
}
