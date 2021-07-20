package com.wsl.socket.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class resultVO {
    /**
     * 消息类型
     * 0：全局提示
     * 10：私聊
     * 20：群聊
     * 30：
     */
    private Integer type;
}
