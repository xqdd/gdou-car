package com.wteam.delay_queue;

import com.wteam.car.bean.entity.Order;
import com.wteam.car.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderJobHandler {


    private final OrderService orderService;

    @Autowired
    public OrderJobHandler(OrderService orderService) {
        this.orderService = orderService;
    }

    public Thread getThread(OrderJob job) {
        return new Thread(job);
    }


    public class Thread implements Runnable {
        private OrderJob job;

        public Thread(OrderJob job) {
            this.job = job;
        }

        @Override
        public void run() {
            Optional<Order> optionalOrder = orderService.findById(job.getId());
            if (!optionalOrder.isPresent()) return;
            Order order = optionalOrder.get();
            if (order.getStatus() == 0 || order.getStatus() == 1) {
                switch (job.getType()) {
                    //订单超时
                    case 0:
                        order.setStatus(4);
                        break;
                    //订单完成
                    case 1:
                        order.setStatus(2);
                        break;
                    default:
                        break;
                }
            }
            orderService.save(order);
        }
    }


}
