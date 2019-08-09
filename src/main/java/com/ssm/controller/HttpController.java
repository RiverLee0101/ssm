package com.ssm.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/httpRequest")
public class HttpController {
    @RequestMapping("/doGet")
    public String doGet(HttpServletRequest request) {
        System.out.println(request.getParameter("name"));
        return "index";
    }
    
}
