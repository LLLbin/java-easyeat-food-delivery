package com.lllebin.controller;

import com.lllebin.domain.User;
import com.lllebin.response.CommonResponse;
import com.lllebin.service.UserService;
import com.lllebin.utils.SMSUtils;
import com.lllebin.utils.ValidateCodeUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: UserController
 * Package: com.lllebin.controller
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @PostMapping("/sendMsg")
    public CommonResponse<String> sengMsg(@RequestBody User user, HttpServletRequest httpServletRequest) {
        log.info("给用户生成验证码：phone = {}", user.getPhone());
        // 1. 获取手机号
        String phone = user.getPhone();
        if (!StringUtils.isNotEmpty(phone)) {
            return CommonResponse.error("手机号为空");
        }

        // 2. 生成随机的4位验证码
        String code = ValidateCodeUtils.generateValidateCode(4).toString();

        // 3. 调用阿里云发送验证码, 做做样子，在log中看就行了（偷懒行为）
        log.info("code = {}", code);
        // SMSUtils.sendMessage("EasyEat", "", phone, code);

        // 4. 存储验证码到session
        // HttpSession session = httpServletRequest.getSession();
        // session.setAttribute(phone, code);

        // 4. pro 存储验证码到Redis，并设置有效期为5分钟
        redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);

        // 5. 把验证码返回浏览器用于回显（偷懒行为）
        return CommonResponse.success(code);
    }

    @PostMapping("/login")
    public CommonResponse<User> login(@RequestBody Map map, HttpServletRequest request) {
        log.info("用户登录：{}", map);

        // 1. 获取手机号，获取验证码
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();

        // 2. 从session获取验证码
        // String codeInSession = (String) request.getSession().getAttribute(phone);

        // 2. pro 从Redis获取验证码
        String codeInSession = (String) redisTemplate.opsForValue().get(phone);

        // 3. 校验验证码
        if (codeInSession == null || !codeInSession.equals(code)) {
            return CommonResponse.error("登录失败");
        }

        // 4. 若新用户，则自动注册，插入数据库中
        List<User> userList = userService.getUserByPhone(phone);
        request.getSession().setAttribute("user", userList.get(0).getId());

        // 5. 登陆成功，返回用户信息
        redisTemplate.delete(phone);
        return CommonResponse.success(userList.get(0));
    }

    @PostMapping("/logout")
    public CommonResponse<String> logout(HttpServletRequest request) {
        log.info("用户登出成功, ID = {}", request.getSession().getAttribute("user"));
        request.getSession().removeAttribute("user");
        return CommonResponse.success("用户登出成功");
    }
}
