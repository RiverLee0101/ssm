package com.ssm.service.impl;

import com.ssm.dao.IUserDao;
import com.ssm.dao.UserMapper;
import com.ssm.model.User;
import com.ssm.service.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
