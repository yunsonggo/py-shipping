package com.pyshipping.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pyshipping.model.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * 员工数据层
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
