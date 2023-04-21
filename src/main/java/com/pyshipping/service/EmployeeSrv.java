package com.pyshipping.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pyshipping.model.Employee;


/**
 * 员工服务层
 */

public interface EmployeeSrv extends IService<Employee> {
    // id 查询员工
    Employee getEmployeeById(String id);
}
