package cn.dmwqaq.chat_room.mapper;

import cn.dmwqaq.chat_room.pojo.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    User getById(String id) throws Exception;

    int insert(User user) throws Exception;

    int update(User user) throws Exception;
}
