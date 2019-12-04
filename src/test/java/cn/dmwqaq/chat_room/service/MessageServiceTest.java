package cn.dmwqaq.chat_room.service;

import cn.dmwqaq.chat_room.pojo.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        {"classpath*:config/springmvc-config.xml"})
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Test
    public void getById() {
        try {
            System.out.println(messageService.getById("1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void create() {
        try {
            Message message = new Message("hello world", "10000", "11111");
            messageService.create(message);
            message.setTarget("all");
            messageService.create(message);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}