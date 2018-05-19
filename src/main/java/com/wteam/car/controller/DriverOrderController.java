package com.wteam.car.controller.driver;

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
 * 司机订单接口
 */
@Controller("driverOrder")
@RequestMapping("driverOrder/")
@Api(name = "司机订单接口", mapping = "/driverOrder/")
public class DriverOrderController {

    private final OrderService orderService;

    @Autowired
    public DriverOrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    @ApiAction(name = "获取可接订单列表", mapping = "getValidOrders", method = Method.POST)
    @PostMapping("getValidOrders")
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
    public Msg getValidOrders(@RequestBody PageInfo pageInfo) {
        return Msg.successData(orderService.findValidOrders(pageInfo));
    }


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
    public Msg getMyOrders(@RequestBody PageInfo pageInfo, @SessionAttribute(name = "user") User driver) {
        return Msg.successData(orderService.findDriverOrders(driver, pageInfo));
    }


    @ApiAction(name = "接订单", mapping = "takeOrder", method = Method.POST)
    @PostMapping("takeOrder")
    @ResponseBody
    @ApiReqParams(
            value = {
                    @ApiParam(name = "driver", required = true, dataType = DataType.OBJECT, object = "driver", description = "乘客信息"),
                    @ApiParam(name = "trueName", required = true, description = "姓名", belongTo = "driver"),
                    @ApiParam(name = "phoneNumber", required = true, description = "电话", belongTo = "driver"),
                    @ApiParam(name = "sex", defaultValue = "1", required = true, description = "用户的性别，值为1时是男性，值为2时是女性", belongTo = "driver"),
                    @ApiParam(name = "id", required = true, description = "订单id"),
            }, type = ParamType.JSON
    )
    @ApiRespParams({
            @ApiParam(name = "code", description = "1操作成功，0操作失败", dataType = DataType.NUMBER),
            @ApiParam(name = "msg", description = "错误或成功消息"),
            @ApiParam(name = "data", description = "成功或错误信息数据", dataType = DataType.OBJECT),
            @ApiParam(name = "debugMsg", description = "调试信息，仅供开发调试时参考，不用理他"),
    })
    public Msg takeOrder(@RequestBody @Validated(OrderGroup.DriverOrder.takeOrder.class) Order takeOrder, @SessionAttribute(name = "user") User driver) {
        //是否存在
        Optional<Order> orderOptional = orderService.findById(takeOrder.getId());
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (order.getStatus() != 0) {
                return Msg.failedDataMsg("id", "订单状态有误，不能取消");
            }
            order.setOrderTime(new Timestamp(System.currentTimeMillis()));
            order.setStatus(1);
            driver.setPhoneNumber(order.getDriver().getPhoneNumber());
            driver.setTrueName(order.getDriver().getTrueName());
            driver.setSex(order.getDriver().getSex());
            order.setDriver(driver);
            orderService.save(order);

            OrderJob orderJob = new OrderJob();
            orderJob.setType(1);
            orderJob.setDelayTime(order.getOrderTime().getTime() + 60 * 60 * 1000);
            OrderJobQueue.addOrderJob(orderJob);

            return Msg.success("操作成功");

        } else {
            return Msg.failedDataMsg("id", "该订单不存在");
        }
    }
}


