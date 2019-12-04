package cn.dmwqaq.chat_room.mapper;

import cn.dmwqaq.chat_room.pojo.Message;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageMapper {

    Message getById(String id) throws Exception;

    int create(Message message) throws Exception;

//    int update(Message message) throws Exception;
}
