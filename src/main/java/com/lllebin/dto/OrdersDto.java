package com.lllebin.dto;


import com.lllebin.domain.OrderDetail;
import com.lllebin.domain.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private List<OrderDetail> orderDetails;
	
}
