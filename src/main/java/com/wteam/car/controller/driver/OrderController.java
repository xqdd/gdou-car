package com.wteam.car.controller.driver;

import com.wteam.car.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * 司机订单接口
 */
@Controller
public class OrderController {

    private final OrderService orderServer;

    @Autowired
    public OrderController(OrderService orderServer) {
        this.orderServer = orderServer;
    }


}
