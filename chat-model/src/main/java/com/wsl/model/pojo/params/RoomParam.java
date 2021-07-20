package com.wsl.model.pojo.params;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author user
 * @since 2021-07-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class RoomParam {

    private static final long serialVersionUID = 1L;

    /**
     * 群账号
     */
    @NotBlank(message = "群账号不能为空")
    private String name;
    /**
     * 群名称
     */
    @NotBlank(message = "群名称不能为空")
    private String nickName;

    /**
     * 群头像
     */
    private String headImg;


    /**
     * 群主
     */
    private String leader;

    /**
     * 简介
     */
    private String detail;

    /**
     * 通知
     */
    private String notice;

}
