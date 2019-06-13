package com.ssm.service;

import com.ssm.model.User;
import com.ssm.model.UserExample;

import java.util.List;

public interface IUserService {
    User selectUser(long userId);
    int insert(User user);
    void addUser(User user);
    List<User> selectAll(UserExample example);
}
