package com.wteam.car.service;

import com.wteam.car.base.impl.BaseServiceImpl;
import com.wteam.car.bean.entity.Feedback;
import com.wteam.car.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class FeedbackService extends BaseServiceImpl<Feedback, Integer>
        implements com.wteam.car.base.BaseService<Feedback, Integer> {

    private FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        super(feedbackRepository);
        this.feedbackRepository = feedbackRepository;
    }

}
