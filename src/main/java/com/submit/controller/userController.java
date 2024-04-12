package com.submit.controller;

import com.submit.pojo.user;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class userController {

    Logger logger = LoggerFactory.getLogger(userController.class);
    @Autowired(required = false)
    com.submit.dao.userMapper userMapper;
    @Autowired(required = false)
    com.submit.service.userService userService;

    @PostMapping("updatepassword")
    public String updatepassword(String username, String oldpassword, String newpassword, Model model) {
        try {
            user user = userMapper.selectByPrimaryKey(username);
            logger.info(user.getUsername() + " " + user.getUserid() + " " + user.getPassword());
            if (user == null) {
                model.addAttribute("msg", "账号错误,非法操作");
                model.addAttribute(new user());
            } else if (!user.getPassword().equals(oldpassword)) {
                model.addAttribute("msg", "密码错误，请重新输入");
                model.addAttribute(user);
            } else {
                userMapper.updatepassword(username, newpassword);
                model.addAttribute("msg", "密码已成功修改，退出登陆后请用新密码登录");
                model.addAttribute(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("msg", "异常错误");
        }
        return "user/changepass";
    }

    @ResponseBody
    @PostMapping("addstuclaid")
    public String addstuclaid(int stuclaid, HttpServletRequest request) {
        try {
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            ;
            return "插入失败";
        }
    }

}
