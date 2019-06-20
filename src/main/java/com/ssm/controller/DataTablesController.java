package com.ssm.controller;


import com.ssm.model.User;
import com.ssm.service.IUserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("dataTables")
public class DataTablesController {
    
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    @ModelAttribute
    public void setReqAndRes(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    
    @Resource
    private IUserService userService;

    @RequestMapping(value = "")
    public String init(){
        return "dataTables";
    }
    
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public void list(@RequestBody List<Object> params){
        System.out.println("list from client:"+params);
        Map<String, Object> requestParams = convertToMap(params);
        System.out.println("map from list:"+requestParams);
        Map<String, Object> innerParams =new HashMap<String, Object>();
        // 将key "start"和"limit" 替换requestMap中的"iDisplayStart"和"iDisplayLength"构造新的map innerParams
        for(String key : requestParams.keySet()){
            if("iDisplayStart".equalsIgnoreCase(key)){
                innerParams.put("start", requestParams.get("iDisplayStart"));
                continue;
            }
            if("iDisplayLength".equalsIgnoreCase(key)){
                innerParams.put("limit", requestParams.get("iDisplayLength"));
                continue;
            }
            innerParams.put(key, requestParams.get(key));
        }
        System.out.println("innerMap:"+innerParams);
        List<? extends Serializable> list = null;
        list = getResultsByQueryParams(innerParams);
        // 将获得的结果list写进table
        writeTable(list, requestParams);
    }
    
    @RequestMapping(value = "/toAddUser", method = RequestMethod.GET)
    public String toAddUser(){
        return "addUser";
    }
    
    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public void addUser(){
        String username=request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRole(role);
        userService.addUser(user);
    }
    
    public List<? extends Serializable> getResultsByQueryParams(Map innerParams){
        return userService.getResultsByQueryParams(innerParams);
    }

    @SuppressWarnings("unchecked")
    public Map<String,Object> convertToMap(List<Object> list){
        Map<String, Object> map = new HashMap<String, Object>();
        for(Object o : list){
            Map<String, Object> param = (Map<String, Object>) o;
            map.put((String) param.get("name"),param.get("value"));
        }
        return map;
    }
    
    public void writeTable(List<? extends Serializable> list, Map requestParams){
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragrma", "no-cache");
        response.setDateHeader("Expires", 0);

        JSONObject result = new JSONObject();
        // 将返回给jsp的json数据加上标识
        result.put("aaData", JSONArray.fromObject(list));
        result.put("iTotalRecords", 14);
        result.put("iTotalDisplayRecords", 14);
        result.put("sEcho", requestParams.get("sEcho"));
        
        System.out.println("Return to client:"+result);
        
        try{
            response.getWriter().write(result.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }
}
