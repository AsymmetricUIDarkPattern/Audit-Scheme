package com.submit.controller;

import com.submit.dao.userMapper;
import com.submit.pojo.user;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class loginController {

    @Autowired(required = false)
    userMapper userMapepr;

    @ResponseBody
    @GetMapping("test")
    public List<user> test() {
        return userMapepr.getall();
    }

    @PostMapping("userlogin")
    public String userlogin(String userid, String password, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().setAttribute("role", "user");
        //2.封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(userid, password);
        //3.执行登录方法
        try {
            subject.login(token);
            //登录成功
            //跳转到test.html
            request.getSession().setAttribute("userid", userid);
            request.getSession().setAttribute("userid", userid);
            return "redirect:user";
        } catch (UnknownAccountException e) {
            //登录失败:用户名不存在
            return "redirect:login.html";
        } catch (IncorrectCredentialsException e) {
            //登录失败:密码错误
            return "redirect:login.html";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:login.html";
        }
    }

    @PostMapping("/")
    public String homepage() {
        return "redirect:login.html";
    }

    @PostMapping("teacherlogin")
    public String teacherlogin(String username, String password, HttpServletRequest request) {
        Subject subject = SecurityUtils.getSubject();
        subject.getSession().setAttribute("role", "teacher");
        //2.封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);

        //3.执行登录方法
        try {
            subject.login(token);
            //登录成功
            //跳转到test.html
            request.getSession().setAttribute("teacherid", username);
            return "redirect:teacher";
        } catch (UnknownAccountException e) {
            //e.printStackTrace();
            //登录失败:用户名不存在
            return "redirect:loginteacher.html";
        } catch (IncorrectCredentialsException e) {
            //e.printStackTrace();
            //登录失败:密码错误
            return "redirect:loginteacher.html";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:loginteacher.html";
        }
    }
}
