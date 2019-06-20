package com.ssm.service.impl;

import com.ssm.dao.IUserDao;
import com.ssm.dao.UserMapper;
import com.ssm.model.User;
import com.ssm.model.UserExample;
import com.ssm.service.IUserService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements IUserService {
    @Resource
    private IUserDao userDao;
    @Resource
    private UserMapper userMapper;
    public User selectUser(long userId) {
        return this.userDao.selectUser(userId);
    }

    @Override
    public int insert(User user) {
        return 0;
    }

    public void addUser(User user){
        userMapper.insert(user);
    }

    public List<User> selectAll(UserExample example){
     return userMapper.selectByExample(example);
    }
    
    public List<? extends Serializable> getResultsByQueryParams(Map innerParams){
        Integer pageStart = Integer.valueOf(innerParams.get("start").toString());
        Integer pageSize = Integer.valueOf(innerParams.get("limit").toString());
        UserExample example = new UserExample();
        
        example.setPageStart(pageStart);
        example.setPageSize(pageSize);
        
        List<User> results = userMapper.selectByExample(example);
        return results;
        
    }
}
