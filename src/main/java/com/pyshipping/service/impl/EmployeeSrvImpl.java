package com.pyshipping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pyshipping.mapper.EmployeeMapper;
import com.pyshipping.model.Employee;
import com.pyshipping.service.EmployeeSrv;
import org.springframework.stereotype.Service;

@Service
public class EmployeeSrvImpl extends ServiceImpl<EmployeeMapper,Employee> implements EmployeeSrv {

    @Override
    public Employee getEmployeeById(String id) {
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getId,id);
        lqw.eq(Employee::getStatus,1);
        return this.getOne(lqw);
    }
}
