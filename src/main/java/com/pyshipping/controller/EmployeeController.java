package com.pyshipping.controller;


import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pyshipping.common.codes.Codes;
import com.pyshipping.common.jwt.Jwt;
import com.pyshipping.common.jwt.JwtBody;
import com.pyshipping.common.msg.Msg;
import com.pyshipping.common.pwd.Pwd;
import com.pyshipping.model.Employee;
import com.pyshipping.service.EmployeeSrv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeSrv srv;

    @Autowired
    private Pwd pwd;

    @Autowired
    private Jwt jwt;

    /**
     * 登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public Msg<Object> login(HttpServletRequest request, @RequestBody Employee employee) {
        // 获取提交参数
        String username = employee.getUsername();
        String password = employee.getPassword();

        // 查询数据库员工数据
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,username);
        Employee res = srv.getOne(lqw);
        if (res == null) {
            return Msg.error(Codes.InvalidArgument,"not found username","found username:"+username+"error");
        }
        // 密码校验
        if (!pwd.check(password,res.getPassword())) {
            return Msg.error(Codes.InvalidArgument,"password error","password error,username:"+username+"error");
        }
        // 账号状态是否禁用
        if (res.getStatus() == 0) {
            return Msg.error(Codes.PermissionDenied,"The account has been disabled","permissions error");
        }

        // 生成token
        String token = jwt.generateJwt(res.getId(),username,res.getPassword(),6969);
        if (Objects.equals(token, "")) {
            return Msg.error(Codes.Internal,"login failed","generate token error");
        }
        res.setPassword("");
        res.setToken(token);
        return Msg.success(Codes.OK,"ok",res);
    }

    /**
     * 退出
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public Msg<Object> logout(HttpServletRequest request) {
        String bearToken ;
        try {
            bearToken = request.getHeader("Authorization");
        } catch (Exception ex) {
            return Msg.success(Codes.OK,"ok","token expired");
        }
        if (bearToken == null || bearToken.equals("") || bearToken.equals("null")) {
            return Msg.success(Codes.OK,"ok","token expired");
        }
        if (!jwt.removeJwt(bearToken))  {
            return Msg.error(Codes.Internal,"logout failed","remove token error");
        } else {
            return Msg.success(Codes.OK,"ok","already logout");
        }
    }

    /**
     * token 获取用户
     * @param request
     * @return
     */
    @GetMapping("/first")
    public Msg<Object> first(HttpServletRequest request) {
        String bearToken ;
        try {
            bearToken = request.getHeader("Authorization");
        } catch (Exception ex) {
            return Msg.error(Codes.DeadlineExceeded,"token expired,please login again","token expired");
        }
        if (bearToken == null || bearToken.equals("") || bearToken.equals("null")) {
            return Msg.error(Codes.DeadlineExceeded,"token expired,please login again","token expired");
        }
        JwtBody jwtBody = jwt.verifyJwt(bearToken);
        if (jwtBody == null) {
            return Msg.error(Codes.DeadlineExceeded,"token expired,please login again","token expired");
        }
        // 查询数据库员工数据
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername,jwtBody.getUsername());
        Employee res = srv.getOne(lqw);
        if (res == null) {
            return Msg.error(Codes.InvalidArgument,"not found username","found username:"+jwtBody.getUsername()+"error");
        }

        // 生成token
        String token = jwt.generateJwt(res.getId(),res.getUsername(),res.getPassword(),res.getStatus());
        if (Objects.equals(token, "")) {
            return Msg.error(Codes.Internal,"login failed","generate token error");
        }
        res.setToken(token);
        res.setPassword("");
        return Msg.success(Codes.OK,"ok",res);
    }

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @PostMapping("/add")
    public Msg<String> add(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("员工数据: {}",employee.toString());
        if (!employee.verifyFields()) {
            return Msg.error(Codes.InvalidArgument,"Please complete the form data","param required");
        }
        String password = pwd.encode(employee.getPassword());
        employee.setPassword(password);

//        JwtBody jwtBody = (JwtBody) request.getAttribute("user");
//        employee.setCreateUser(jwtBody.getUserID());
//        employee.setUpdateUser(jwtBody.getUserID());
        srv.save(employee);
        return Msg.success(Codes.OK,"ok","successful");
    }

    /**
     * 分页
     * @param page
     * @param size
     * @param name
     * @return
     */
    @GetMapping("/list")
    public Msg<Page> list(Integer page, Integer size, String name) {
        // 分页构造器
        Page<Employee> limited = new Page<Employee> (page,size);
        // 条件构造器
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<> ();
        // like 参数自动判断 是否添加该条件参与查询
        lqw.select(Employee.class, fieldInfo -> !fieldInfo.getColumn().equals("password"));
        lqw.like(!StringUtils.isEmpty(name),Employee::getName,name);
        // 排序
        lqw.orderByDesc(Employee::getUpdateTime);
        // 查询
        srv.page(limited,lqw);

        return Msg.success(Codes.OK,"ok",limited);
    }

    /**
     * ID 修改员工
     * @param request
     * @param employee
     * @return
     */
    @PutMapping("")
    public Msg<String> edit(HttpServletRequest request, @RequestBody Employee employee) {
        //修改员工数据
//        JwtBody jwtBody = (JwtBody) request.getAttribute("user");
//        employee.setUpdateUser(jwtBody.getUserID());
        employee.setPassword(null);
        srv.updateById(employee);
        return Msg.success(Codes.OK,"ok","ok");
    }
}
