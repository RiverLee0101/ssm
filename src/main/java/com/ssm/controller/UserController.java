package com.ssm.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.model.User;
import com.ssm.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;

    @RequestMapping("/showUser.do")
    public void selectUser(HttpServletRequest request, HttpServletResponse response) throws IOException{
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        long userId = Long.parseLong(request.getParameter("id"));
        User user = this.userService.selectUser(userId);
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(user));
        response.getWriter().close();
    }

    @RequestMapping("/showAllUsers")
    public void showAllUsers(String param){
        System.out.println(param);
    }

    @RequestMapping("/addUser")
    public void addUser(HttpServletRequest request, Model model){
        User user = new User();
        user.setUsername(String.valueOf(request.getParameter("name")));
        user.setPassword(String.valueOf(request.getParameter("password")));
        userService.addUser(user);
    }
}
