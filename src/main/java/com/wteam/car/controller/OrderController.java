package com.wteam.car.controller;

import com.apidoc.annotation.*;
import com.apidoc.enumeration.Method;
import com.apidoc.enumeration.ParamType;
import com.wteam.car.bean.entity.Feedback;
import com.wteam.car.bean.entity.Order;
import com.wteam.car.bean.interact.response.Msg;
import com.wteam.car.service.FeedbackService;
import com.wteam.car.service.OrderService;
import com.wteam.car.utils.jsonview.OrderGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController("order")
@RequestMapping(value = "order", produces = "application/json; charset=utf-8")
@Api(name = "订单通用接口/其他", mapping = "order/")
public class OrderController {


    private final OrderService orderService;

    private final FeedbackService feedbackService;

    @Autowired
    public OrderController(OrderService orderService, FeedbackService feedbackService) {
        this.orderService = orderService;
        this.feedbackService = feedbackService;
    }

    //获取我的订单列表
    @ApiAction(name = "获取订单详情", mapping = "getOrderDetail", method = Method.POST)
    @PostMapping("getOrderDetail")
    @ApiReqParams(
            value = {
                    @ApiParam(name = "id", description = "订单id")
            }, type = ParamType.JSON
    )
    @ApiRespParams
    public Msg getOrders(@RequestBody @Validated(OrderGroup.id.class) Order order) {
        return Msg.successData(orderService.findById(order.getId()));
    }


    //获取我的订单列表
    @ApiAction(name = "反馈", mapping = "feedback", method = Method.POST)
    @PostMapping("feedback")
    @ApiReqParams(
            value = {
                    @ApiParam(name = "content", description = "反馈内容")
            }, type = ParamType.JSON
    )
    @ApiRespParams
    public Msg feedback(@RequestBody @Validated Feedback feedback) {
        feedback.setSendTime(new Date());
        feedbackService.save(feedback);
        return Msg.success("操作成功");
    }
}
