package com.usian.controller;

import com.usian.api.UserFeign;
import com.usian.pojo.TbUser;
import com.usian.utils.CookieUtils;
import com.usian.utils.Result;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/frontend/sso")
public class SSOController {
    @Autowired
    private UserFeign userFeign;

    @Autowired
    private AmqpTemplate amqpTemplate;
    //注册验证
    @RequestMapping("/checkUserInfo/{checkValue}/{checkFlag}")
    public Result checkUserInfo(@PathVariable String checkValue,@PathVariable Integer checkFlag){
       Boolean checkUserInfo = userFeign.checkUserInfo(checkValue,checkFlag);
       if (checkUserInfo){
           return Result.ok();
       }
       return Result.error("校验失败");
    }


    //注册
    @RequestMapping("/userRegister")
    public Result userRegister(TbUser user){
        try {
            userFeign.userRegister(user);
            return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    //登录

    @RequestMapping("/userLogin")
    public Result userLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request){
        try {
            Map userInfo = userFeign.userLogin(username,password);
            if (userInfo==null){
               return Result.error("登陆失败");
            }
            Long userId = userFeign.userId(username);

//            Long userId1 = Long.valueOf(String.valueOf(userInfo.get("userId")));

            String cartJson = CookieUtils.getCookieValue(request, "CART", true);
            HashMap<Long, String> data = new HashMap<>();
            data.put(userId,cartJson);
            amqpTemplate.convertAndSend("sso_exchange", "sso.insert",data);

            return Result.ok(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error(e.getMessage());
        }
    }

    //校验token是否是有效的
    @RequestMapping("/getUserByToken/{token}")
    public Result getUserByToken(@PathVariable("token") String token){
        Boolean result = userFeign.getUserByToken(token);
        if (result){
            return Result.ok();
        }
        return Result.error("token过期了");
    }






}
