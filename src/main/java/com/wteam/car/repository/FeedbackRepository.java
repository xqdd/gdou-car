package com.wteam.car.repository;

import com.wteam.car.base.BaseRepository;
import com.wteam.car.bean.entity.Feedback;
import org.springframework.stereotype.Repository;


@Repository
public interface FeedbackRepository extends BaseRepository<Feedback, Integer> {

}
