package com.wteam.car.service;

import com.wteam.car.base.impl.BaseServiceImpl;
import com.wteam.car.bean.entity.Order;
import com.wteam.car.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrderService extends BaseServiceImpl<Order, Integer>
        implements com.wteam.car.base.BaseService<Order, Integer> {

    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
    }

}
