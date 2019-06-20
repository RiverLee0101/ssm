package com.ssm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.model.User;
import com.ssm.model.UserExample;
import com.ssm.service.IUserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
public class UserController {
    @Resource
    private IUserService userService;

    // 启动程序即列表展示所有用户信息
    @RequestMapping(value = "")
    public String listAll(Model model){
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        List<User> users = userService.selectAll(example);
        System.out.println("UserController:"+users);
        model.addAttribute("users", users);
        return "index";
    }
    // 增
    @RequestMapping(value = "/addUser",method = RequestMethod.POST)
    public String addUser(HttpServletRequest request, Model model){
        User user = new User();
        Date date = new Date();
        user.setUsername(String.valueOf(request.getParameter("name")));
        user.setPassword(String.valueOf(request.getParameter("password")));
        user.setEmail(String.valueOf(request.getParameter("email")));
        user.setRegtime(date);
        userService.addUser(user);
        return "redirect:/"; //重定向URL
    }
    // ajax请求数据
    @RequestMapping(value = "/ajaxRequest",method = RequestMethod.POST)
    public void sendString(HttpServletRequest request,HttpServletResponse response){
        String name = request.getParameter("name");
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        List<User> users = userService.selectAll(example);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("users", JSONArray.fromObject(users));
        System.out.println(jsonObject);
        try {
            response.getWriter().write(jsonObject.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

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

    @RequestMapping("/doGetControllerOne")
    public String doGetControllerOne(){
        System.out.println("yes");
        return "123";
    }
}
