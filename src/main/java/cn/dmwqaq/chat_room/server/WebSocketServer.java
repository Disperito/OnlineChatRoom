package cn.dmwqaq.chat_room.server;

import cn.dmwqaq.chat_room.pojo.Message;
import cn.dmwqaq.chat_room.pojo.vo.MessageVO;
import cn.dmwqaq.chat_room.service.MessageService;
import cn.dmwqaq.chat_room.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Component
@ServerEndpoint("/webSocket/{userId}")
public class WebSocketServer {

    /**
     * 当前在线人数
     */
    private static int onlineCount = 0;

    /**
     * Key: 每位用户的ID
     * Value: 每位用户所持有的WebSocket服务端对象
     */
    private static Map<String, WebSocketServer> clients = new ConcurrentHashMap<>();

    /**
     * Key: 用户ID
     * Value: 对应的用户名
     */
    private static Map<String, String> userInfoMap = new ConcurrentHashMap<>();

    /**
     * Key: 在线的用户ID
     * Value: 对应的用户名
     */
    private static Map<String, String> onlineUsers = new ConcurrentHashMap<>();

    /**
     * 当前用户的ID
     */
    private String userId;

    private static Logger logger = LogManager.getLogger(WebSocketServer.class);

    private Session session;

    @Resource
    private UserService userService;

    @Resource
    private MessageService messageService;

    private static WebSocketServer staticThis;

    @PostConstruct
    public void init() {
        staticThis = this;
    }

    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        this.userId = userId;
        this.session = session;

        updateOnlineUserInformation(OnlineStatusChangeEvent.ONLINE);
        sendRecentChatRecord();
    }

    @OnClose
    public void onClose() {
        updateOnlineUserInformation(OnlineStatusChangeEvent.OFFLINE);
    }

    @OnMessage
    public void onMessage(String messageJsonString) {
        JSONObject messageJson = JSONObject.parseObject(messageJsonString);
        if ("all".equals(messageJson.get("target"))) {
            sendMessageToAll(messageJsonString);
        } else {
            sendMessage(messageJsonString, messageJson.get("target").toString());
        }

        Message message = new Message(messageJson.getString("content"), messageJson.getString("source"),
                                      messageJson.getString("target"));
        try {
            staticThis.messageService.create(message);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error(error.getMessage(), error);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized Map<String, WebSocketServer> getClients() {
        return clients;
    }

    private void sendMessageToAll(String message) {
        for (WebSocketServer item : clients.values()) {
            try {
                item.session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void updateOnlineUserInformation(OnlineStatusChangeEvent event) {
        try {
            if (event == OnlineStatusChangeEvent.OFFLINE) {
                clients.remove(userId);
                onlineUsers.remove(userId);
                subOnlineCount();
                logger.trace(userId + "已断开，当前用户数：" + onlineCount);
            } else if (event == OnlineStatusChangeEvent.ONLINE) {
                clients.put(userId, this);
                onlineUsers.put(userId, getUserNameById(userId));
                addOnlineCount();
                logger.trace(userId + "已连接，当前用户数：" + onlineCount);
            }
            notifyAllOnlineStatus();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    private void sendMessage(String message, String target) {
        // session.getBasicRemote().sendText(message);
        //session.getAsyncRemote().sendText(message);
        for (WebSocketServer item : clients.values()) {
            if (item.userId.equals(target)) {
                try {
                    item.session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }

    private void notifyAllOnlineStatus() {
        JSONObject o = new JSONObject();
        o.put("onlineUsers", onlineUsers);
        o.put("target", "all");
        o.put("type", "notify");
        o.put("function", "updateOnlineList");

        sendMessageToAll(o.toJSONString());
    }

    private String getUserNameById(String key) {
        String name;
        if ((name = userInfoMap.get(key)) == null) {
            try {
                name = staticThis.userService.getById(key).getName();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                name = "null";
            }
            userInfoMap.put(key, name);
        }
        return name;
    }

    private void sendRecentChatRecord() {
        List<Message> messages;
        List<MessageVO> messageVOs = null;
        try {
            messages = staticThis.messageService.listByTarget(this.userId);
            messageVOs = messages.stream()
                                 .map(MessageVO::parse)
                                 .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        JSONObject o = new JSONObject();
        o.put("messages", messageVOs);
        o.put("type", "record");
        o.put("target", userId);
        logger.trace(o);
        sendMessage(o.toJSONString(), userId);
    }

    private static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

    private enum OnlineStatusChangeEvent {
        ONLINE,
        OFFLINE
    }
}
