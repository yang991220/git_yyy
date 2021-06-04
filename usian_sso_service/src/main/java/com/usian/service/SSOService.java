package com.usian.service;

import com.usian.pojo.TbUser;

import java.util.Map;

public interface SSOService {
    boolean checkUserInfo(String checkValue, Integer checkFlag);

    void userRegister(TbUser user);

    Map userLogin(String username, String password);

    boolean getUserByToken(String token);

    Long userId(String username);
}
