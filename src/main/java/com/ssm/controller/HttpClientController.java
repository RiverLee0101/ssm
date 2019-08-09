package com.ssm.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/httpClient")
public class HttpClientController {
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    
    @RequestMapping(value = "/doGet", method = RequestMethod.GET)
    public String doGetResponse(){
        return "123";
//        response.setContentType("application/json;charset=UTF-8");
//        response.setHeader("Cache-Control", "no-store");
//        response.setHeader("Pragrma", "no-cache");
//        response.setDateHeader("Expires", 0);
//        try{
//            response.getWriter().write("1123");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }
    
    @RequestMapping("/doGetControllerOne")
    public String doGetControllerOne(){
        return "123";
    }
}
