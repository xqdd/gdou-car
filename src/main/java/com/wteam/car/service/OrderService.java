package com.wteam.car.service;

import com.wteam.car.base.impl.BaseServiceImpl;
import com.wteam.car.bean.interact.request.PageInfo;
import com.wteam.car.bean.entity.Order;
import com.wteam.car.bean.entity.User;
import com.wteam.car.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


@Service
public class OrderService extends BaseServiceImpl<Order, String>
        implements com.wteam.car.base.BaseService<Order, String> {

    private OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        super(orderRepository);
        this.orderRepository = orderRepository;
    }


    public long test() {
        Specification<Order> specification = new Specification<Order>() {
            @Override
            public Predicate toPredicate(Root<Order> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                return query.where(criteriaBuilder.equal(root.get("status"), 0)).getRestriction();
            }
        };
        return orderRepository.count(specification);
    }

    public long unCompleteCount(User passenger) {
        return orderRepository.countByPassengerAndStatus(passenger, 0);
    }

    public List<Order> findPassengerOrders(User passenger, PageInfo pageInfo) {
        return orderRepository.findAllByPassengerOrderByCreateTime(passenger, PageRequest.of(pageInfo.getCurrPage() - 1, pageInfo.getPageSize()));
    }

    public List<Order> findValidOrders(PageInfo pageInfo) {
        return orderRepository.findAllByStatusOrderByCreateTime(0, PageRequest.of(pageInfo.getCurrPage() - 1, pageInfo.getPageSize()));
    }

    public List<Order> findDriverOrders(User driver, PageInfo pageInfo) {
        return orderRepository.findAllByDriverOrderByCreateTime(driver, PageRequest.of(pageInfo.getCurrPage() - 1, pageInfo.getPageSize()));
    }
}
