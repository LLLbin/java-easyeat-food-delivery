package com.lllebin.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.lllebin.domain.*;
import com.lllebin.dto.OrdersDto;
import com.lllebin.exception.ServiceException;
import com.lllebin.exception.ServiceExceptionCode;
import com.lllebin.mapper.OrdersMapper;
import com.lllebin.response.PageResponse;
import com.lllebin.utils.BaseContextUtils;
import com.lllebin.utils.SnowflakeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ClassName: OrderService
 * Package: com.lllebin.service
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Service
public class OrdersService {

    @Autowired
    private SnowflakeUtils snowflakeUtils;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Transactional(rollbackFor = RuntimeException.class)
    public void submit(Orders orders) {
        // 1. 获得用户Id
        Long userId = BaseContextUtils.getCurrentId();

        // 2. 查询当前用户的购物车数据，用户信息，地址信息，明细数据
        // 2-1 地址信息
        List<ShoppingCart> shoppingCartList = shoppingCartService.listByUserId();
        if (shoppingCartList == null || shoppingCartList.size() == 0) {
            throw new ServiceException(ServiceExceptionCode.TARGET_NOT_EXISTS);
        }
        // 2-2 用户信息
        User user = userService.getById();
        // 2-3 地址信息
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());
        if (addressBook == null) {
            throw new ServiceException(ServiceExceptionCode.TARGET_NOT_EXISTS);
        }
        // 2-4 明细数据
        long orderId = snowflakeUtils.nextId();

        AtomicInteger amount = new AtomicInteger(0);

        List<OrderDetail> orderDetailList = shoppingCartList.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).toList();


        // 3. 向订单表插入数据，1条数据
        orders.setId(orderId);
        orders.setNumber(String.valueOf(snowflakeUtils.nextId()));
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(user.getId());
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(
                (addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName()) +
                (addressBook.getCityName() == null ? "" : addressBook.getCityName()) +
                (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName()) +
                (addressBook.getDetail() == null ? "" : addressBook.getDetail())
        );

        ordersMapper.insertSelective(orders);

        // 4. 向订单明细表插入数据，多条数据
        orderDetailList.forEach((orderDetail) -> {
            orderDetailService.save(orderDetail);
        });

        // 5. 清空购物车
        shoppingCartService.deleteByUserId();
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void submitAgain(Orders orders) {
        long orderId = snowflakeUtils.nextId();

        // 1. 向订单表插入数据，1条数据
        Orders newOrders = ordersMapper.selectByPrimaryKey(orders.getId());
        newOrders.setId(orderId);
        newOrders.setStatus(2);
        newOrders.setNumber(String.valueOf(snowflakeUtils.nextId()));
        newOrders.setOrderTime(LocalDateTime.now());
        newOrders.setCheckoutTime(LocalDateTime.now());
        ordersMapper.insertSelective(newOrders);

        // 2. 向订单明细表插入数据，多条数据
        List<OrderDetail> orderDetailList = orderDetailService.getOrderDetailByOrderId(orders.getId());
        orderDetailList.forEach((item) -> {
            item.setOrderId(orderId);
            orderDetailService.save(item);
        });
    }

    public PageResponse<OrdersDto> selectUserPage(int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        OrdersExample ordersExample = new OrdersExample();
        ordersExample.setOrderByClause("order_time DESC");
        List<Orders> orders = ordersMapper.selectByExample(ordersExample);
        Page<Orders> p = (Page<Orders>) orders;

        List<OrdersDto> ordersDtoList = orders.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            List<OrderDetail> orderDetailByOrderId = orderDetailService.getOrderDetailByOrderId(item.getId());
            ordersDto.setOrderDetails(orderDetailByOrderId);
            return ordersDto;
        }).toList();

        return new PageResponse<>(p.getTotal(), ordersDtoList);
    }

    public PageResponse<OrdersDto> selectManagementPage(int page, int pageSize, String number, LocalDateTime beginTime, LocalDateTime endTime) {
        PageHelper.startPage(page, pageSize);
        OrdersExample ordersExample = new OrdersExample();
        OrdersExample.Criteria criteria = ordersExample.createCriteria();

        if (number != null) {
            criteria.andNumberLike("%" + number + "%");
        }

        if (beginTime != null && endTime != null) {
            criteria.andOrderTimeBetween(beginTime, endTime);
        } else if (beginTime != null) {
            criteria.andOrderTimeGreaterThan(beginTime);
        } else if (endTime != null){
            criteria.andOrderTimeLessThan(endTime);
        }

        ordersExample.setOrderByClause("order_time DESC");
        List<Orders> orders = ordersMapper.selectByExample(ordersExample);
        Page<Orders> p = (Page<Orders>) orders;

        List<OrdersDto> ordersDtoList = orders.stream().map((item) -> {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            List<OrderDetail> orderDetailByOrderId = orderDetailService.getOrderDetailByOrderId(item.getId());
            ordersDto.setOrderDetails(orderDetailByOrderId);
            return ordersDto;
        }).toList();

        return new PageResponse<>(p.getTotal(), ordersDtoList);
    }

    public Orders updateStatusById(Orders orders) {
        ordersMapper.updateByPrimaryKeySelective(orders);
        return ordersMapper.selectByPrimaryKey(orders.getId());
    }


}
