package com.lllebin.controller;

import com.lllebin.domain.Employee;
import com.lllebin.response.CommonResponse;
import com.lllebin.response.PageResponse;
import com.lllebin.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

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
     * @param employee
     * @return
     */
    @PostMapping
    public CommonResponse<String> save(@RequestBody Employee employee) {
        log.info("新增员工, 员工信息:{}", employee.toString());
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
        log.info("查询员工信息，page = {}, pageSize = {}, name = {}", page, pageSize, name);
        PageResponse<Employee> employeePageResponse = employeeService.pageQuery(page, pageSize, name);
        return CommonResponse.success(employeePageResponse);
    }

    /**
     * 更新员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public CommonResponse<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("更新员工，员工信息：{}", employee);
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
