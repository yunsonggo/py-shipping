package com.pyshipping.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.pyshipping.common.codes.Codes;
import com.pyshipping.common.jwt.Jwt;
import com.pyshipping.common.keys.Keys;
import com.pyshipping.common.msg.Msg;
import com.pyshipping.common.pwd.Pwd;
import com.pyshipping.model.User;
import com.pyshipping.service.UserSrv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserSrv srv;

    @Autowired
    private Pwd pwd;

    @Autowired
    private Jwt jwt;

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Autowired
    private Keys keys;

    @PostMapping("/login")
    public Msg<User> login(@RequestBody Map map) {
        String phone = map.get("phone").toString();
        String smsCode = map.get("code").toString();
        String username = map.get("username").toString();
        if (username == null || username.equals("")) {
            username = "游客";
        }
        //  redis验证
        String smsKey = keys.getSmsRedisKey(phone);
        Object redisCode = redisTemplate.opsForValue().get(smsKey);
        if (redisCode == null) {
            return Msg.error(Codes.DeadlineExceeded,"验证码过期,请重新发送","sms code expired");
        }
        if (!smsCode.equals(redisCode)) {
            return Msg.error(Codes.InvalidArgument,"验证码错误,请稍后重试","sms code error");
        }
        // if redisCode == null && !smsCode.equals(redisCode)
        // return Msg.error(Codes.OK,"登录失败","验证码错误")
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getPhone,phone);
        User user = srv.getOne(lqw);
        if (user == null) {
            // 新用户自动注册
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            srv.save(user);
        }
        // 生成token
        String password = user.getPassword();
        if (password == null || password.equals("")) {
            password = "";
        }
        String token = jwt.generateJwt(user.getId(),user.getUsername(),password,1);
        user.setToken(token);
        user.setPassword("");
        // 删除redis code
        redisTemplate.delete(smsKey);
        return Msg.success(Codes.OK,"登录成功",user);
    }
}
