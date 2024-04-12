package com.submit.controller;

import com.submit.pojo.user;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class thymleafController {
    @Autowired(required = false)
    com.submit.dao.userMapper userMapper;
    @Autowired(required = false)
    com.submit.service.userService userService;

    @GetMapping("user")
    public String index(Model model, HttpServletRequest request) {
        String userId = (String)request.getSession().getAttribute("userid");
        model.addAttribute("userid", userId);
        try{
            user user = userMapper.selectByPrimaryKey(userId);
            model.addAttribute("username", user.getUsername());
            model.addAttribute("level", user.getLevel());
        } catch (Exception e){
            model.addAttribute("username", userId);
            model.addAttribute("level", 0);

        }
        return "user/user";
    }

    @GetMapping("changepass")
    public String changepassword(Model model, HttpServletRequest request) {
        user user = userMapper.selectByPrimaryKey((String) request.getSession().getAttribute("userid"));
        model.addAttribute("msg", "");
        model.addAttribute(user);
        return "user/changepass";
    }

    @GetMapping("uploadjilu")
    public String uploadjilu() {
        return "user/addteach";
    }


}
