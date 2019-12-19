package cn.dmwqaq.chat_room.mapper;

import cn.dmwqaq.chat_room.pojo.Message;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageMapper {

    Message getById(String id) throws Exception;

    int create(Message message) throws Exception;

    List<Message> listByTarget(String target) throws Exception;
//    int update(Message message) throws Exception;
}
