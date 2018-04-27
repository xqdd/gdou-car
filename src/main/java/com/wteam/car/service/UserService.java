package com.wteam.car.service;

import com.wteam.car.base.impl.BaseServiceImpl;
import com.wteam.car.bean.entity.User;
import com.wteam.car.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService extends BaseServiceImpl<User, String>
        implements com.wteam.car.base.BaseService<User, String> {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        super(userRepository);
        this.userRepository = userRepository;
    }

}
