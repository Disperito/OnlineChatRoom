package cn.dmwqaq.chat_room.service;

import cn.dmwqaq.chat_room.pojo.Message;

public interface MessageService {

    Message getById(String id) throws Exception;

    boolean create(Message message) throws Exception;

//    boolean update(Message message) throws Exception;

}
