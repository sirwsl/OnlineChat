package com.wsl.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

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
@TableName("t_room")
public class Room extends Model<Room> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 群账号
     */
    private String name;
    /**
     * 群名称
     */
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

    /**
     * 房间人员
     */
    private String userIds;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    private Boolean delFlag;


    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String NICK_NAME = "nick_name";

    public static final String HEAD_IMG = "head_img";


    public static final String LEADER = "leader";

    public static final String DETAIL = "detail";

    public static final String NOTICE = "notice";

    public static final String USER_IDS = "user_ids";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String DEL_FLAG = "del_flag";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
