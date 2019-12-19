package cn.dmwqaq.chat_room.service;

import cn.dmwqaq.chat_room.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        {"classpath*:config/springmvc-config.xml"})
public class UserServiceTest {

    @Resource
    UserMapper userMapper;

    @Test
    public void getById() {
        try {
            System.out.println(userMapper.getById("10000"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}