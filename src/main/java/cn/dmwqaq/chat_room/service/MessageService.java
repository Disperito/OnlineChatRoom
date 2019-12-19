package cn.dmwqaq.chat_room.service;

import cn.dmwqaq.chat_room.pojo.Message;

import java.util.List;

public interface MessageService {

    Message getById(String id) throws Exception;

    boolean create(Message message) throws Exception;

    List<Message> listByTarget(String target) throws Exception;
//    boolean update(Message message) throws Exception;

}
