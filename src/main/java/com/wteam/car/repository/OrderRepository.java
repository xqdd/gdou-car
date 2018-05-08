package com.wteam.car.repository;

import com.wteam.car.base.BaseRepository;
import com.wteam.car.bean.entity.Order;
import com.wteam.car.bean.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends BaseRepository<Order, String> {

    long countByPassengerAndStatus(User passenger, Integer status);

    List<Order> findAllByPassengerOrderByCreateTime(User passenger, Pageable pageable);

}
