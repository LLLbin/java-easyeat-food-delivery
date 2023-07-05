package com.lllebin.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lllebin.exception.ServiceException;
import com.lllebin.exception.ServiceExceptionCode;
import com.lllebin.response.CommonResponse;
import com.lllebin.domain.Employee;
import com.lllebin.response.PageResponse;
import com.lllebin.service.EmployeeService;
import com.lllebin.utils.Snowflake;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * ClassName: EmployeeController
 * Package: com.lllebin.controller
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private Snowflake snowflake;

    /**
     * 登录账号
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public CommonResponse<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        // md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 查询数据库
        List<Employee> emps = employeeService.selectByUsername(employee);

        // 是否登陆失败
        if (emps == null) {
            return CommonResponse.error("登录失败");
        }

        Employee emp = emps.get(0);

        // 密码比对
        if (!emp.getPassword().equals(password)) {
            return CommonResponse.error("登录失败");
        }

        // 查看状态
        if (emp.getStatus() == 0) {
            return CommonResponse.error("账号已禁用");
        }

        // 登录成功
        log.info("用户登录成功,ID:{}", emp.getId());
        request.getSession().setAttribute("employee", emp.getId());
        return CommonResponse.success(emp);
    }

    /**
     * 退出账号
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public CommonResponse<String> logout(HttpServletRequest request) {
        log.info("用户退出成功,ID:{}", request.getSession().getAttribute("employee"));
        request.getSession().removeAttribute("employee");
        return CommonResponse.success("退出成功");
    }

    /**
     * 新增员工
     * @param request
     * @param employee
     * @return
     */
    @PostMapping
    public CommonResponse<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工, 员工信息:{}", employee.toString());
        // 设置员工信息
        employee.setId(snowflake.nextId());
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long empId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        // 插入数据库中
        employeeService.save(employee);
        return CommonResponse.success("新增员工成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public CommonResponse<PageResponse<Employee>> page(int page, int pageSize, String name) {
        log.info("page = {}, pageSize = {}, name = {}", page, pageSize, name);
        PageHelper.startPage(page, pageSize);
        List<Employee> employeeList = employeeService.selectByName(name);
        Page<Employee> p = (Page<Employee>) employeeList;

        return CommonResponse.success(new PageResponse<>(p.getTotal(), p.getResult()));
    }

    /**
     * 更新员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public CommonResponse<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("更新员工，员工信息：{}", employee);

        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return CommonResponse.success("员工修改成功");
    }

    /**
     * 根据ID查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public CommonResponse<Employee> getUserById(@PathVariable Long id) {
        log.info("根据ID查询员工信息：{}", id);
        Employee employee = employeeService.getUserById(id);
        return CommonResponse.success(employee);
    }
}
