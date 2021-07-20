package com.wsl.socket.server;

import com.alibaba.fastjson.JSONObject;
import com.wsl.model.constant.RedisEnum;
import com.wsl.model.pojo.bos.ChatUserBO;
import com.wsl.model.pojo.bos.UserBO;
import com.wsl.socket.pojo.MsgParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/socket/{userId}")
@Slf4j
@Component
public class WebSocketServer {

    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static Map<String,Session> idForSession = new ConcurrentHashMap<>(256);

    private static RedisTemplate redisTemplate;

    private static RedisTemplate<String, UserBO> userRedisTemplate;

    @Autowired
    private void setStringRedisTemplate(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    private void setRedisTemplate(RedisTemplate redisTemplate){
        this.userRedisTemplate = redisTemplate;
    }

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("userId") String userId) {
        idForSession.put(userId,session);
        //广播
        UserBO userBO = userRedisTemplate.opsForValue().get(RedisEnum.USER_VERIFY+userId);
        if (Objects.nonNull(userBO)){
            idForSession.values().stream().filter(Session::isOpen).forEach(li-> {
                try {
                    li.getBasicRemote().sendText(userBO.getNickName());
                } catch (IOException e) {
                    log.warn("通知上线消息失败");
                    e.printStackTrace();
                }
            });
            redisTemplate.boundValueOps(RedisEnum.NUM_NOW).increment(1);// 在线数加1
        }

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("userId") String userId) {
        try {
            redisTemplate.boundValueOps(RedisEnum.NUM_NOW).decrement(1);
        }catch (Exception e){
            log.warn("在线人数递减失败");
        }
        idForSession.remove(userId);
    }

}
