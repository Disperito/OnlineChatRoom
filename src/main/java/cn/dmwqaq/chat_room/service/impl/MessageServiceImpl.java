package cn.dmwqaq.chat_room.service.impl;

import cn.dmwqaq.chat_room.mapper.MessageMapper;
import cn.dmwqaq.chat_room.pojo.Message;
import cn.dmwqaq.chat_room.service.MessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Override
    public Message getById(String id) throws Exception {
        return messageMapper.getById(id);
    }

    @Override
    public boolean create(Message message) throws Exception {
        if (message.getCreateTime() == null) {
            message.setCreateTime(new Date());
        }
        return messageMapper.create(message) > 0;
    }

//    @Override
//    public boolean update(Message message) throws Exception {
//        return messageMapper.update(message) > 0;
//    }

}
