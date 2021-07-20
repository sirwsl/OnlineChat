package com.wsl.socket.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.websocket.Session;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class BaseSocketObj{

    /**
     * 用户id
     */
    private String id;

    /**
     * 用户昵称
     */
    private String nickName;


    private Session session;
}
