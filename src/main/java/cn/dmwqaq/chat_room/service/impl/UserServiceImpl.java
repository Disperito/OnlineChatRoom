package cn.dmwqaq.chat_room.service.impl;

import cn.dmwqaq.chat_room.mapper.UserMapper;
import cn.dmwqaq.chat_room.pojo.User;
import cn.dmwqaq.chat_room.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public boolean update(User user) throws Exception {
        return userMapper.update(user) > 0;
    }

    @Override
    public boolean create(User user) throws Exception {
        return userMapper.create(user) > 0;
    }

    @Override
    public User getById(String id) throws Exception {
        return userMapper.getById(id);
    }

}
