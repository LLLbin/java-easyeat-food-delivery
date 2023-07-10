package com.lllebin.service;

import com.lllebin.domain.OrderDetail;
import com.lllebin.domain.OrderDetailExample;
import com.lllebin.mapper.OrderDetailMapper;
import com.lllebin.utils.SnowflakeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.util.List;

/**
 * ClassName: OrdersDetailService
 * Package: com.lllebin.service
 * Description:
 *
 * @author : Lebin Zhou
 * @version : 1.0
 */
@Service
public class OrderDetailService {

    @Autowired
    private SnowflakeUtils snowflakeUtils;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    public void save(OrderDetail orderDetail) {
        orderDetail.setId(snowflakeUtils.nextId());
        orderDetailMapper.insertSelective(orderDetail);
    }

    public List<OrderDetail> getOrderDetailByOrderId(Long orderId) {
        OrderDetailExample orderDetailExample = new OrderDetailExample();
        OrderDetailExample.Criteria criteria = orderDetailExample.createCriteria();
        criteria.andOrderIdEqualTo(orderId);
        return orderDetailMapper.selectByExample(orderDetailExample);
    }

}
