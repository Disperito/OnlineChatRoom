package cn.dmwqaq.chat_room.controller;

import cn.dmwqaq.chat_room.pojo.User;
import cn.dmwqaq.chat_room.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserController {

    private static Logger logger = LogManager.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @PostMapping(value = "/login")
    public void login(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/html");

        String userId = request.getParameter("id");
        String password = request.getParameter("password");
        User user = null;

        try {
            user = userService.getById(userId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        try {
            if (user == null || !password.equals(user.getPassword())) {
                response.getWriter().write("false");
            } else {
                response.getWriter().write("true");
                request.getSession().setAttribute("userId", userId);
                request.getSession().setAttribute("userName", user.getName());
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @DeleteMapping(value = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            request.getSession().removeAttribute("userId");
            request.getSession().removeAttribute("userName");
            response.setContentType("text/html");
            response.getWriter().write("true");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            response.getWriter().write("false");
        }
    }

    @PostMapping(value = "/register", produces = "text/html; charset=UTF-8")
    @ResponseBody
    public String register(String username, String password) {

        User user = new User(username, password);
        try {
            if (userService.insert(user)) {
                return "注册成功，您的ID是" + user.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "注册失败";
    }

}
