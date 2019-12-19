package cn.dmwqaq.chat_room.pojo.vo;

import cn.dmwqaq.chat_room.pojo.Message;
import cn.dmwqaq.chat_room.pojo.User;
import cn.dmwqaq.chat_room.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;

@Component
public class MessageVO {

    private static Logger logger = LogManager.getLogger(MessageVO.class);

    private String id;
    private String content;
    private String sourceName;
    private String target;
    private java.util.Date createTime;
    private boolean withdrawn;

    @Resource
    private UserService userService;

    private static MessageVO staticThis;

    @PostConstruct
    public void init() {
        staticThis = this;
    }

    public MessageVO() {
    }

    public MessageVO(String id, String content, String sourceName, String target, Date createTime,
                     boolean withdrawn) {
        this.id = id;
        this.content = content;
        this.sourceName = sourceName;
        this.target = target;
        this.createTime = createTime;
        this.withdrawn = withdrawn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isWithdrawn() {
        return withdrawn;
    }

    public void setWithdrawn(boolean withdrawn) {
        this.withdrawn = withdrawn;
    }

    public static MessageVO parse(Message message) {
        var o = new MessageVO();

        String sourceName = "";
        try {
            User user = staticThis.userService.getById(message.getSourceUserId());
            if (user != null) {
                sourceName = user.getName();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        o.content = message.getContent();
        o.createTime = message.getCreateTime();
        o.id = message.getId();
        o.sourceName = sourceName;
        o.target = message.getTarget();
        o.withdrawn = message.isWithdrawn();

        return o;
    }
}
