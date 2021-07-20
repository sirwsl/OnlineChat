package com.wsl.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

/**
 * <p>
 * 
 * </p>
 *
 * @author user
 * @since 2021-07-02
 */
@Data
@Accessors(chain = true)
@TableName("t_user")
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 用户名
     */
    private String name;

    private String password;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 昵称
     */
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

    /**
     * 权限
     */
    private String authority;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Boolean delFlag;



    public static final String AUTHORITY = "authority";

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String PASSWORD = "password";

    public static final String HEAD_IMG = "head_img";

    public static final String NICK_NAME = "nick_name";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String DEL_FLAG = "del_flag";

    public static final String QQ = "qq";

    public static final String WX = "wx";

    public static final String EMAIL = "email";

    public static final String PHONE = "phone";

    public static final String SEX = "sex";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
