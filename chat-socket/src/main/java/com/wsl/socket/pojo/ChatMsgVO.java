package com.wsl.socket.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ChatMsgVO {

    private String type;
    /**
     * 来自谁
     */
    private String formId;

    /**
     * 接收人
     */
    private String toId;

    /**
     * 消息id
     */
    private String _id;
    /**
     * 时间戳
     */
    private String date;
    /**
     * 消息发送者
     */
    private User user;

    /**
     * 消息
     */
    private Message message;

    @Data
    @Accessors(chain = true)
    public static class User {
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
        private String desc;
    }

    @Data
    @Accessors(chain = true)
    public static class Message {
        /**
         * 类型
         */
        private String type;
        /**
         * 内容
         */
        private String content;
    }

}
