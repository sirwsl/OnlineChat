package com.wsl.socket.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MsgParam {

    private String type;

    private String id;

    private ChatMsgVO data;


}
