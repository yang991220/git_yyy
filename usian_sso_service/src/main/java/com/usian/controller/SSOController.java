package com.usian.controller;

import com.usian.pojo.TbUser;
import com.usian.service.SSOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
public class SSOController {
    @Autowired
    private SSOService ssoService;

    //校验token是否是有效的
    @RequestMapping("/getUserByToken/{token}")
    public boolean getUserByToken(@PathVariable("token") String token) {
        return ssoService.getUserByToken(token);
    }

    //登录
    @RequestMapping("/userLogin")
    public Map userLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        return ssoService.userLogin(username, password);
    }

    //注册
    @RequestMapping("/checkUserInfo/{checkValue}/{checkFlag}")
    public boolean checkUserInfo(@PathVariable String checkValue, @PathVariable Integer checkFlag) {
        return ssoService.checkUserInfo(checkValue, checkFlag);
    }

    //注册验证
    @RequestMapping("/userRegister")
    public void userRegister(@RequestBody TbUser user) {
        ssoService.userRegister(user);
    }

    //查询用户id
    @RequestMapping("/userId")
    public Long userId(String username){
       return ssoService.userId(username);
    }

}
