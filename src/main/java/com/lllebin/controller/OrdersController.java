package com.lllebin.controller;

import com.lllebin.domain.Orders;
import com.lllebin.dto.OrdersDto;
import com.lllebin.response.CommonResponse;
import com.lllebin.response.PageResponse;
import com.lllebin.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * ClassName: OrderController
 * Package: com.lllebin.controller
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */

@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @PostMapping("submit")
    public CommonResponse<String> submit(@RequestBody Orders orders) {
        log.info("创建订单, {}", orders);
        ordersService.submit(orders);
        return CommonResponse.success("创建订单成功");
    }

    @GetMapping("/userPage")
    public CommonResponse<PageResponse<OrdersDto>> userPage(int page, int pageSize) {
        log.info("查看用户订单, page = {}, pageSize = {}", page, pageSize);
        PageResponse<OrdersDto> ordersPageResponse = ordersService.selectUserPage(page, pageSize);
        return CommonResponse.success(ordersPageResponse);
    }

    @GetMapping("/page")
    public CommonResponse<PageResponse<OrdersDto>> page(int page, int pageSize, String number, LocalDateTime beginTime, LocalDateTime endTime) {
        log.info("查看用户订单, page = {}, pageSize = {}, number = {}, beginTime = {}, endTime = {}", page, pageSize, number, beginTime, endTime);
        PageResponse<OrdersDto> ordersPageResponse = ordersService.selectManagementPage(page, pageSize, number, beginTime, endTime);
        return CommonResponse.success(ordersPageResponse);
    }

    @PutMapping
    public CommonResponse<Orders> updateStatus(@RequestBody Orders orders) {
        log.info("修改用户订单状态, orders = {}", orders);
        Orders newOrders = ordersService.updateStatusById(orders);
        return CommonResponse.success(newOrders);
    }

    @PostMapping("/again")
    public CommonResponse<String> again(@RequestBody Orders orders) {
        log.info("再来一单, {}", orders);
        ordersService.submitAgain(orders);
        return CommonResponse.success("创建订单成功");
    }

}
