package com.wsl.model.pojo.bos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ChatUserBO {
    private String type;
    /**
     * id
     */
    private String id;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 签名
     */
    private String message;
    /**
     * 时间
     */
    private String date;

}
