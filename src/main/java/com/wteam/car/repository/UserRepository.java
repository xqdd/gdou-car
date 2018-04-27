package com.wteam.car.repository;

import com.wteam.car.base.BaseRepository;
import com.wteam.car.bean.entity.User;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends BaseRepository<User, String> {

}
