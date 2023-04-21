package com.pyshipping.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pyshipping.mapper.UserMapper;
import com.pyshipping.model.User;
import com.pyshipping.service.UserSrv;
import org.springframework.stereotype.Service;

@Service
public class UserSrvImpl extends ServiceImpl<UserMapper, User> implements UserSrv {
}
