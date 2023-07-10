package com.lllebin.filter;

/**
 * ClassName: LoginCheckFilter
 * Package: com.lllebin.filter
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */

import com.alibaba.fastjson.JSON;
import com.lllebin.response.CommonResponse;
import com.lllebin.utils.BaseContextUtils;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

/**
 * 检查用户是否完成登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    // 路径匹配器,支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. 获取URL
        String requestURI = request.getRequestURI();

        // log.info("拦截到请求:{}", requestURI);

        // 2. 判断是否放行
        if (isPermitted(requestURI)) {
            log.info("本次请求不需要处理:{}", requestURI);
            filterChain.doFilter(request, response);
            return;
        }

        // 3-1. 判断工作人员是否完成登录
        Long employeeId = (Long) request.getSession().getAttribute("employee");
        if (employeeId != null) {
            log.info("工作人员已登录:{}, employeeId = {}", requestURI, employeeId);

            // 获取当前用户ID，并保存在ThreadLocal
            BaseContextUtils.setCurrentId(employeeId);

            filterChain.doFilter(request, response);
            return;
        }

        // 3-2. 判断用户是否完成登录
        Long userId = (Long) request.getSession().getAttribute("user");
        if (userId != null) {
            log.info("用户已登录:{}, userId = {}", requestURI, userId);

            // 获取当前用户ID，并保存在ThreadLocal
            BaseContextUtils.setCurrentId(userId);

            filterChain.doFilter(request, response);
            return;
        }

        // 4. 通过输出流向客户端页面响应数据(没有@ResponseBody注释, 需要自己手动返回JSON格式数据)
        log.info("用户未登录:{}", requestURI);
        response.getWriter().write(JSON.toJSONString(CommonResponse.error("NOTLOGIN")));
    }

    private boolean isPermitted(String requestURI) {
        String[] urls = new String[] {
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/swagger-ui/**",
                "/v3/**",
                "/user/sendMsg",
                "/user/login",
                "/user/logout"
        };
        for (String url : urls) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
