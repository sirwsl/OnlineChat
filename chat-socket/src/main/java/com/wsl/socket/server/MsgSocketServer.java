package com.wsl.socket.server;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.wsl.model.constant.RedisEnum;
import com.wsl.model.pojo.bos.ChatUserBO;
import com.wsl.model.pojo.bos.UserBO;
import com.wsl.service.service.FriendsRoomService;
import com.wsl.socket.pojo.MsgParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/chatMsg/{userId}")
@Slf4j
@Component
public class MsgSocketServer{

    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static Map<String,Session> idForSession = new ConcurrentHashMap<>(256);

    private static RedisTemplate<String, ChatUserBO> chatUserRedisTemplate;

    private static FriendsRoomService friendsRoomService;

    @Autowired
    private void setFriendsRoomService(FriendsRoomService friendsRoomService){
        this.friendsRoomService = friendsRoomService;
    }

    @Autowired
    private void setChatUserRedisTemplate(RedisTemplate redisTemplate){
        this.chatUserRedisTemplate = redisTemplate;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId) {
        idForSession.put(userId,session);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        idForSession.remove(userId);
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message
     *            客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session,@PathParam("userId") String userId){
        MsgParam msgParam = JSONObject.parseObject(message,MsgParam.class);

        if (Objects.nonNull(msgParam)){
            if ("msg".equals(msgParam.getType())){
                //获取发送人session
                Session session1 = idForSession.get(msgParam.getData().getToId());
                msgParam.getData().setType("msg");
                msgParam.getData().setFormId(userId);
                if (ObjectUtils.isNotEmpty(session1)){
                    sendMessage(session1,JSONObject.toJSONString(msgParam.getData()));
                }else {
                    //如果是群聊转发到对应群聊
                    msgParam.getData().setFormId(msgParam.getData().getToId());
                    List<String> userIdForChat = friendsRoomService.getUserIdForChat(msgParam.getData().getToId());
                    userIdForChat.remove(userId);
                    if (CollectionUtils.isNotEmpty(userIdForChat)){
                        userIdForChat.forEach(li ->{
                            Session temp = idForSession.get(li);
                            if (Objects.nonNull(temp)){
                                sendMessage(temp,JSONObject.toJSONString(msgParam.getData()));
                            }
                        });
                    }else{
                        //如果不是签名两种情况，说明是不在线
                        sendMessage(session,"{type:'error',msg:'消息已发送，但是对方不在线'}");
                    }
                }
            }else{
                //获取人
                ChatUserBO chatUserBO = chatUserRedisTemplate.opsForValue().get(RedisEnum.USER_CHAT_BO+msgParam.getId());
                if (Objects.nonNull(chatUserBO)){
                    chatUserBO.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd hh:mm")));
                    sendMessage(session,chatUserBO.toString());
                }else{//如果人为空，那就获取群
                    ChatUserBO bo = friendsRoomService.getChatUserBO(msgParam.getId());
                    if (Objects.nonNull(bo)){
                        sendMessage(session,bo.toString());
                    }
                }
            }
        }

    }

    /**
     * 发送消息
     * @param session:
     * @param message:
     */
    public static void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("发送消息出错：{}", e.getMessage());
            e.printStackTrace();
        }
    }


}
