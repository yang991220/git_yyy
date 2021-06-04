package com.usian.service.impl;

import com.usian.mapper.TbUserMapper;
import com.usian.pojo.TbUser;
import com.usian.service.SSOService;
import com.usian.util.RedisClient;
import com.usian.utils.JwtUtils;
import com.usian.utils.MD5Utils;
import com.usian.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SSOServiceImpl implements SSOService {
    @Autowired
    private TbUserMapper tbUserMapper;
    @Autowired
    private RedisClient redisClient;



    @Override
    public boolean checkUserInfo(String checkValue, Integer checkFlag) {
        TbUser tbUser = new TbUser();//查询条件对象
        if (checkFlag == 1) { //用户名
            tbUser.setUsername(checkValue);
        } else {
            tbUser.setPhone(checkValue);
        }
        List<TbUser> select = tbUserMapper.select(tbUser);
        if (select == null && select.size() == 0) {
            return true;
        }
        return false;

    }

    @Override
    public void userRegister(TbUser user) {
        user.setPassword(MD5Utils.digest(user.getPassword()));
        user.setCreated(new Date());
        tbUserMapper.insertSelective(user);

    }

    @Override
    public Map userLogin(String username, String password) {
        HashMap map = new HashMap<>();

        //取数据做对比
        TbUser tbUser = new TbUser();//查询条件对象
        tbUser.setUsername(username);
//        tbUser.setPassword(MD5Utils.digest(password));
        List<TbUser> select = tbUserMapper.select(tbUser);
        //对比成功 留痕迹
        if (select != null && select.size() > 0) {
            if (select.get(0).getPassword().equals(MD5Utils.digest(password))) {
//存redis  什么结构来存
                //key token value 用户
//                String token = UUID.randomUUID().toString();
//                redisClient.set(token, select.get(0));
//                redisClient.expire(token, 60 * 60 * 24);


                //JWT 规范生成一个token
                //获取私钥
               String privateKey = (String) redisClient.get("PRIVATE_KEY");
                byte[] privateKeyArr =  StringUtils.toByteArray(privateKey);
                try {
                   String token = JwtUtils.generateToken(select.get(0),privateKeyArr,60*30);
                    map.put("token", token);
                    map.put("userid", select.get(0).getId());
                    map.put("username", select.get(0).getUsername());
                    map.put("flag", true);



                } catch (Exception e) {
                    e.printStackTrace();
                }



                //存cookie???  返回浏览器需要的信息  前端自己存入到cookie


                return map;
            } else {
                map.put("flag", false);
                map.put("msg", "密码输入错误");
            }
        } else {//用户名输入错误了
            map.put("flag", false);
            map.put("msg", "用户名输入错误");

        }
        return null;


    }


    //校验token是否是有效的
    @Override
    public boolean getUserByToken(String token) {

        String publicKey = (String) redisClient.get("PUBLIC_KEY");
        try {
            TbUser tbUser = JwtUtils.getInfoFromToken(token, StringUtils.toByteArray( publicKey));
            if (tbUser==null){
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
//        Object user = redisClient.get(token);
//        if (user == null) {
//            return false;
//        }
//
//        return true;
    }

    @Override
    public Long userId(String username) {
        TbUser tbUser = new TbUser();
        tbUser.setUsername(username);
        List<TbUser> tbUsers = tbUserMapper.select(tbUser);
        Long id = tbUsers.get(0).getId();
        return id;
    }
}
