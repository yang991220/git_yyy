package com.usian.api;

import com.usian.pojo.TbUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("usian-sso-service")
public interface UserFeign {

    //校验token是否是有效的
    @RequestMapping("/user/getUserByToken/{token}")
    public boolean getUserByToken(@PathVariable("token") String token);

    //登录
    @RequestMapping("/user/userLogin")
    public Map userLogin(@RequestParam("username") String username, @RequestParam("password") String password);

    //注册
    @RequestMapping("/user/checkUserInfo/{checkValue}/{checkFlag}")
    public boolean checkUserInfo(@PathVariable String checkValue, @PathVariable Integer checkFlag);

    //注册验证
    @RequestMapping("/user/userRegister")
    public void userRegister(@RequestBody TbUser user);

    //查询用户id
    @RequestMapping("/user/userId")
    Long userId(@RequestParam("username") String username);
}
