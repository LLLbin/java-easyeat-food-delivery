package com.lllebin.service;

import com.lllebin.domain.Employee;
import com.lllebin.domain.EmployeeExample;
import com.lllebin.exception.ServiceException;
import com.lllebin.exception.ServiceExceptionCode;
import com.lllebin.mapper.EmployeeMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.rmi.server.ServerCloneException;
import java.util.Date;
import java.util.List;

/**
 * ClassName: EmployeeService
 * Package: com.lllebin.service
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    public List<Employee> selectByUsername(Employee employee) {
        EmployeeExample employeeExample = new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        criteria.andUsernameEqualTo(employee.getUsername());

        return employeeMapper.selectByExample(employeeExample);
    }

    public void save(Employee employee) {
        EmployeeExample employeeExample = new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        criteria.andUsernameEqualTo(employee.getUsername());

        // 用户已存在 -> 抛出异常
        List<Employee> employees = employeeMapper.selectByExample(employeeExample);
        if (employees.size() > 0) {
            throw new ServiceException(ServiceExceptionCode.TARGET_ALREADY_EXISTS);
        }

        employeeMapper.insertSelective(employee);
    }

    public List<Employee> selectByName(String name) {
        EmployeeExample employeeExample = new EmployeeExample();
        EmployeeExample.Criteria criteria = employeeExample.createCriteria();
        if (StringUtils.isNotEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        employeeExample.setOrderByClause("name DESC");
        return employeeMapper.selectByExample(employeeExample);
    }

    public void updateById(Employee employee) {
        employeeMapper.updateByPrimaryKeySelective(employee);
    }

    public Employee getUserById(Long id) {
        Employee employee = employeeMapper.selectByPrimaryKey(id);
        // 用户不存在 -> 抛出异常
        if (employee == null) {
            throw new ServiceException(ServiceExceptionCode.TARGET_NOT_EXISTS);
        }
        return employee;
    }
}
