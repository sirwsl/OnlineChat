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
@TableName("t_friend_room")
public class FriendRoom extends Model<FriendRoom> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    private String userId;

    /**
     * 我关注的
     */
    private String friendIds;

    /**
     * 关注我的
     */
    private String roomIds;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Boolean delFlag;


    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String FRIENDS_IDS = "friend_ids";

    public static final String ROOM_IDS = "room_ids";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String DEL_FLAG = "del_flag";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
