package cn.dmwqaq.chat_room.socket;

import cn.dmwqaq.chat_room.service.UserService;
import com.alibaba.fastjson.JSONObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    @Autowired
    private UserService userService;

    //    private static UserService staticUserService;

    //    private final UserService userService;

    //    public WebSocketServer(UserService userService) {
    //        this.userService = userService;
    //    }
    private static WebSocketServer staticThis;

    @PostConstruct
    public void init() {
        //        staticUserService = this.userService;
        staticThis = this;
    }

    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {
        this.userId = userId;
        this.session = session;

        updateOnlineUserInformation(OnlineStatusChangeEvent.ONLINE);
    }

    @OnClose
    public void onClose() {
        updateOnlineUserInformation(OnlineStatusChangeEvent.OFFLINE);
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

    @OnMessage
    public void onMessage(String messageJsonString) {
        JSONObject messageJson = JSONObject.parseObject(messageJsonString);
        if ("all".equals(messageJson.get("target"))) {
            sendMessageToAll(messageJsonString);
        } else {
            sendMessage(messageJsonString, messageJson.get("target").toString());
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
        if ((name = userInfoMap.get(key)) != null) {
            return name;
        } else {
            try {
                name = staticThis.userService.getById(key).getName();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                name = "null";
            }
            userInfoMap.put(key, name);
            return name;
        }
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
