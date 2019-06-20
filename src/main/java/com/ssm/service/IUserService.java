package com.ssm.service;

import com.ssm.model.User;
import com.ssm.model.UserExample;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface IUserService {
    User selectUser(long userId);
    int insert(User user);
    void addUser(User user);
    List<User> selectAll(UserExample example);
    List<? extends Serializable> getResultsByQueryParams(Map innerParams);
}
