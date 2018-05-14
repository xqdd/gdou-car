package com.wteam.car.controller.passenger;

import com.apidoc.annotation.*;
import com.apidoc.enumeration.DataType;
import com.apidoc.enumeration.Method;
import com.apidoc.enumeration.ParamType;
import com.wteam.car.bean.interact.response.Msg;
import com.wteam.car.bean.interact.request.PageInfo;
import com.wteam.car.bean.entity.Order;
import com.wteam.car.bean.entity.User;
import com.wteam.car.service.OrderService;
import com.wteam.car.utils.jsonview.OrderGroup;
import com.wteam.delay_queue.OrderJob;
import com.wteam.delay_queue.OrderJobQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * 乘客订单接口
 */
@Controller("passengerOrder")
@RequestMapping("passengerOrder/")
@Api(name = "乘客订单接口", mapping = "/passengerOrder/")
public class OrderController {

    private final OrderService orderService;


    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @ApiAction(name = "发起订单", mapping = "add", method = Method.POST)
    @ApiReqParams(
            type = ParamType.JSON,
            value = {
                    @ApiParam(name = "passenger",  required = true, dataType = DataType.OBJECT, object = "passenger", description = "乘客信息"),
                    @ApiParam(name = "trueName",  required = true, description = "姓名", belongTo = "passenger"),
                    @ApiParam(name = "phoneNumber",  required = true, description = "电话", belongTo = "passenger"),
                    @ApiParam(name = "departure",  required = true, description = "出发地"),
                    @ApiParam(name = "destination",  required = true, description = "目的地"),
                    @ApiParam(name = "price",  required = true, dataType = DataType.NUMBER, description = "价格"),
                    @ApiParam(name = "appointmentTime",  dataType = DataType.DATE, required = true, description = "预约上车时间点，unix时间戳。单位：milliseconds"),
                    @ApiParam(name = "validTime",  required = true, dataType = DataType.NUMBER, description = "从预约上车时间点开始该订单有效时长。单位：秒"),
                    @ApiParam(name = "ps",  description = "备注"),
            })
    @ApiRespParams({
            @ApiParam(name = "code", description = "1操作成功，0操作失败", dataType = DataType.NUMBER),
            @ApiParam(name = "msg", description = "错误或成功消息"),
            @ApiParam(name = "data", description = "成功或错误信息数据", dataType = DataType.OBJECT),
            @ApiParam(name = "debugMsg", description = "调试信息，仅供开发调试时参考，不用理他"),
    })
    @PostMapping("add")
    public Msg add(@RequestBody @Validated(OrderGroup.PassengerOrder.add.class) Order order
            , @SessionAttribute(name = "user") User passenger) {
        //防止乘客恶意刷单
        if (orderService.unCompleteCount(passenger) >= 5) {
            return Msg.failed("操作失败，你还有5个订单未完成，请待完成或取消后再试");
        }
        passenger.setTrueName(order.getPassenger().getTrueName());
        passenger.setPhoneNumber(order.getPassenger().getPhoneNumber());
        order.setPassenger(passenger);
        order.setStatus(0);
        order.setCreateTime(new Timestamp(System.currentTimeMillis()));
        orderService.save(order);
        //超时处理
        handleOrderTimeOut(order);
        return Msg.successData("添加成功");
    }


    private void handleOrderTimeOut(Order order) {
        OrderJob orderJob = new OrderJob();
        orderJob.setDelayTime(order.getAppointmentTime().getTime() + order.getValidTime() * 1000);
        orderJob.setId(order.getId());
        orderJob.setType(OrderJob.Type.ORDER_CANCEL_TIME_OUT.ordinal());
        OrderJobQueue.addOrderJob(orderJob);
    }

    //取消订单
    @ApiAction(name = "取消订单", mapping = "cancel", method = Method.POST)
    @ApiReqParams(
            type = ParamType.JSON,
            value = {
                    @ApiParam(name = "id",  required = true, description = "订单编号id"),
            })
    @ApiRespParams({
            @ApiParam(name = "code", description = "1操作成功，0操作失败", dataType = DataType.NUMBER),
            @ApiParam(name = "msg", description = "错误或成功消息"),
            @ApiParam(name = "data", description = "成功或错误信息数据", dataType = DataType.OBJECT),
            @ApiParam(name = "debugMsg", description = "调试信息，仅供开发调试时参考，不用理他"),
    })
    @PostMapping("cancel")
    public Msg cancel(@RequestBody @Validated(OrderGroup.id.class) Order orderId,
                      @SessionAttribute(name = "user") User passenger) {
        //是否存在
        Optional<Order> orderOptional = orderService.findById(orderId.getId());
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (!order.getPassenger().getUnionId().equals(passenger.getUnionId())) {
                return Msg.failedDataMsg("id", "该订单不属于改乘客");
            }
            if (order.getStatus() != 0) {
                return Msg.failedDataMsg("id", "订单状态有误，不能取消");
            }
            order.setCancelTime(new Timestamp(System.currentTimeMillis()));
            order.setStatus(3);
            orderService.save(order);
            return Msg.success("操作成功");

        } else {
            return Msg.failedDataMsg("id", "该订单不存在");
        }
    }


    //完成订单
    @ApiAction(name = "完成订单", mapping = "complete", method = Method.POST)
    @ApiReqParams(
            type = ParamType.JSON,
            value = {
                    @ApiParam(name = "id",  required = true, description = "订单编号id"),
            })
    @ApiRespParams({
            @ApiParam(name = "code", description = "1操作成功，0操作失败", dataType = DataType.NUMBER),
            @ApiParam(name = "msg", description = "错误或成功消息"),
            @ApiParam(name = "data", description = "成功或错误信息数据", dataType = DataType.OBJECT),
            @ApiParam(name = "debugMsg", description = "调试信息，仅供开发调试时参考，不用理他"),
    })
    @PostMapping("complete")
    public Msg complete(@RequestBody @Validated(OrderGroup.id.class) Order orderId,
                        @SessionAttribute(name = "user") User passenger) {
        //是否存在
        Optional<Order> orderOptional = orderService.findById(orderId.getId());
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (!order.getPassenger().getUnionId().equals(passenger.getUnionId())) {
                return Msg.failedDataMsg("id", "该订单不属于该乘客");
            }
            if (order.getStatus() != 1) {
                return Msg.failedDataMsg("id", "订单状态有误，不能完成");
            }
            order.setCompleteTime(new Timestamp(System.currentTimeMillis()));
            order.setStatus(2);
            orderService.save(order);
            return Msg.success("操作成功");

        } else {
            return Msg.failedDataMsg("id", "该订单不存在");
        }
    }

    //订单重置
    @ApiAction(name = "订单重置", mapping = "reset", method = Method.POST)
    @ApiReqParams(
            type = ParamType.JSON,
            value = {
                    @ApiParam(name = "id",  required = true, description = "订单编号id"),
            })
    @ApiRespParams({
            @ApiParam(name = "code", description = "1操作成功，0操作失败", dataType = DataType.NUMBER),
            @ApiParam(name = "msg", description = "错误或成功消息"),
            @ApiParam(name = "data", description = "成功或错误信息数据", dataType = DataType.OBJECT),
            @ApiParam(name = "debugMsg", description = "调试信息，仅供开发调试时参考，不用理他"),
    })
    @PostMapping("reset")
    public Msg reset(@RequestBody @Validated(OrderGroup.id.class) Order orderId,
                     @SessionAttribute(name = "user") User passenger) {
        //是否存在
        Optional<Order> orderOptional = orderService.findById(orderId.getId());
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (!order.getPassenger().getUnionId().equals(passenger.getUnionId())) {
                return Msg.failedDataMsg("id", "该订单不属于该乘客");
            }
            if (order.getStatus() != 1) {
                return Msg.failedDataMsg("id", "订单状态有误，不能重置");
            }
            //改变旧订单状态
            order.setStatus(5);
            orderService.save(order);
            //发起新订单
            order.setId(null);
            order.setOrderTime(null);
            order.setStatus(0);
            order.setDriver(null);
            orderService.save(order);
            handleOrderTimeOut(order);
            return Msg.success("操作成功");

        } else {
            return Msg.failedDataMsg("id", "该订单不存在");
        }
    }


    //获取我的订单列表
    @ApiAction(name = "获取我的订单列表", mapping = "getMyOrders", method = Method.POST)
    @PostMapping("getMyOrders")
    @ResponseBody
    @ApiReqParams(
            value = {
                    @ApiParam(name = "pageSize", defaultValue = "10", dataType = DataType.NUMBER, description = "每页条目数"),
                    @ApiParam(name = "currPage", defaultValue = "1", dataType = DataType.NUMBER, description = "当前页，即要获取的页")
            }, type = ParamType.JSON
    )
    @ApiRespParams({
            @ApiParam(name = "code", description = "1操作成功，0操作失败", dataType = DataType.NUMBER),
            @ApiParam(name = "msg", description = "错误或成功消息"),
            @ApiParam(name = "data", description = "成功或错误信息数据", dataType = DataType.OBJECT),
            @ApiParam(name = "debugMsg", description = "调试信息，仅供开发调试时参考，不用理他"),
    })
    public Msg getOrders(@RequestBody PageInfo pageInfo, @SessionAttribute(name = "user") User passenger) {
        return Msg.successData(orderService.findPassengerOrders(passenger, pageInfo));
    }
}
