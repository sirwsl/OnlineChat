package com.wsl.model.pojo.params;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class BaseUserParam {
    /**
     * 用户名
     */
    @NotBlank(message = "id不能为空")
    private String id;

    @NotBlank(message = "账号不能为空")
    private String name;

    @NotNull(message = "性别不能为空")
    private Boolean sex;

    /**
     * 头像
     */
    @NotBlank(message = "头像不能为空")
    private String headImg;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickName;
}
