package com.wteam.car.repository;

import com.wteam.car.base.BaseRepository;
import com.wteam.car.bean.entity.Order;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderRepository extends BaseRepository<Order, Integer> {

}
