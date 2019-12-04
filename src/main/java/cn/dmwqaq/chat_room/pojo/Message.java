package cn.dmwqaq.chat_room.pojo;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

@SuppressWarnings("unused")
public class Message implements Serializable {

    private static final long serialVersionUID = 74831215612364L;

    private String id;
    private String content;
    private String sourceUserId;
    private String target;
    private java.util.Date createTime;
    private boolean withdrawn;

    /**
     * Default constructor
     */
    public Message(String id, String content, String sourceUserId, String target, Date createTime,
            boolean withdrawn) {
        this.id = id;
        this.content = content;
        this.sourceUserId = sourceUserId;
        this.target = target;
        this.createTime = createTime;
        this.withdrawn = withdrawn;
    }

    /**
     * Constructor for JDBC
     */
    public Message(BigInteger id, java.sql.Timestamp createTime, String content, BigInteger sourceUserId,
            String target, Boolean withdrawn) {
        this.id = id.toString();
        this.content = content;
        this.sourceUserId = sourceUserId.toString();
        this.target = target;
        this.createTime = new java.util.Date(createTime.getTime());
        this.withdrawn = withdrawn;
    }

    public Message(String content, String sourceUserId, String target) {
        this.content = content;
        this.sourceUserId = sourceUserId;
        this.target = target;
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

    public String getSourceUserId() {
        return sourceUserId;
    }

    public void setSourceUserId(String sourceUserId) {
        this.sourceUserId = sourceUserId;
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

    @Override
    public String toString() {
        return "Message{" + "id='" + id + '\'' + ", content='" + content + '\'' + ", sourceUserId='" + sourceUserId
                + '\'' + ", targetUserId='" + target + '\'' + ", createTime=" + createTime + ", withdrawn="
                + withdrawn + '}';
    }
}
