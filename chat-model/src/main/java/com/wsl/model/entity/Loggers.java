package com.wsl.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName("t_loggers")
public class Loggers extends Model<Loggers> {

    private static final long serialVersionUID = 1L;

    /**
     * 日志id
     */
    @TableId(type = IdType.ID_WORKER_STR)
    private String id;

    /**
     * 操作内容
     */
    private String detail;

    /**
     * 操作人id
     */
    private String manId;

    /**
     * 操作类型
     */
    private Integer type;

    /**
     * 等级(0-正常 1-良好 2-严重 3-极其严重)
     */
    private Integer grade;

    /**
     * ip
     */
    private String ip;

    /**
     * 创建时间(操作时间)
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Boolean delFlag;


    public static final String ID = "id";

    public static final String DETAIL = "detail";

    public static final String MAN_ID = "man_id";

    public static final String TYPE = "type";

    public static final String GRADE = "grade";

    public static final String IP = "ip";

    public static final String CREAT_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    public static final String DEL_FLAG = "del_flag";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
