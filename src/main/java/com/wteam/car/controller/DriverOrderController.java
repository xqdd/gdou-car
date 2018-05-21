package com.wteam.car.controller;

import com.apidoc.annotation.*;
import com.apidoc.enumeration.DataType;
import com.apidoc.enumeration.Method;
import com.apidoc.enumeration.ParamType;
import com.wteam.car.bean.entity.Order;
import com.wteam.car.bean.entity.User;
import com.wteam.car.bean.interact.request.PageInfo;
import com.wteam.car.bean.interact.response.Msg;
import com.wteam.car.service.OrderService;
import com.wteam.car.service.UserService;
import com.wteam.car.utils.jsonview.OrderGroup;
import com.wteam.delay_queue.OrderJob;
import com.wteam.delay_queue.OrderJobQueue;
import com.wteam.wx.bean.TemplateMsg;
import com.wteam.wx.utils.CommUtils;
import com.wteam.wx.utils.TemplateUtils;
import com.wteam.wx.utils.WxUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

/**
 * 司机订单接口
 */
@RestController("driverOrder")
@RequestMapping(value = "driverOrder", produces = "application/json; charset=utf-8")
@Api(name = "司机订单接口", mapping = "driverOrder/")
public class DriverOrderController {

    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public DriverOrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }


    @ApiAction(name = "获取可接订单列表", mapping = "getValidOrders", method = Method.POST)
    @PostMapping("getValidOrders")
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

        //是否已有订单
        if (orderService.driverOrderUnCompleteCount(driver) >= 5) {
            return Msg.failed("操作失败，您之前有多个订单尚未完成，请待完成后再试");
        }

        //是否存在
        Optional<Order> orderOptional = orderService.findById(takeOrder.getId());
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();

            if (driver.getUnionId().equals(order.getPassenger().getUnionId())) {
                return Msg.failed("操作失败，不能自己接自己的单");
            }

            if (order.getStatus() != 0) {
                return Msg.failedDataMsg("id", "订单状态有误，不能取消");
            }
            order.setOrderTime(new Timestamp(System.currentTimeMillis()));
            order.setStatus(1);
            driver.setPhoneNumber(takeOrder.getDriver().getPhoneNumber());
            driver.setTrueName(takeOrder.getDriver().getTrueName());
            driver.setSex(takeOrder.getDriver().getSex());
            order.setDriver(driver);

            userService.save(driver);
            orderService.save(order);

            OrderJob orderJob = new OrderJob();
            orderJob.setType(1);
            orderJob.setDelayTime(order.getOrderTime().getTime() + 20 * 60 * 1000);
            OrderJobQueue.addOrderJob(orderJob);


            SimpleDateFormat dateFormat = CommUtils.DATE_TIME_FORMAT;

            //发送模板消息
            //给司机
            TemplateMsg sendToDriverMsg = new TemplateMsg();
            sendToDriverMsg.setTemplate_id("CfmQgzumutPL5Ustz6U5IAVtxznJEt4E2QcUV2ESfdo");

            TemplateMsg.Data sendToDriverData = new TemplateMsg.Data();
            sendToDriverData.setFirst(new TemplateMsg.DataValue("接单成功通知"));
            sendToDriverData.setKeyword1(new TemplateMsg.DataValue(order.getDeparture()));
            sendToDriverData.setKeyword2(new TemplateMsg.DataValue(order.getDestination()));
            sendToDriverData.setKeyword3(new TemplateMsg.DataValue(dateFormat.format(order.getAppointmentTime()) + " 到 " + dateFormat.format(new Date(order.getAppointmentTime().getTime() + order.getValidTime() * 1000))));
            sendToDriverData.setKeyword4(new TemplateMsg.DataValue("1人：" + order.getPassenger().getTrueName()));
            sendToDriverData.setKeyword5(new TemplateMsg.DataValue(order.getPassenger().getPhoneNumber()));
            sendToDriverData.setRemark(new TemplateMsg.DataValue("请及时联系乘客确认出发时间和地点"));
            sendToDriverMsg.setData(sendToDriverData);


            sendToDriverMsg.setTouser(driver.getWebOpenid());
            TemplateUtils.sendTemplateMsg(sendToDriverMsg, WxUtils.fetchAccessToken().getAccess_token());

            //给乘客
            TemplateMsg sendToPassengerMsg = new TemplateMsg();
            sendToPassengerMsg.setTemplate_id("7r95QOugJRPyiLUNyQ4FYWob3as7Op4y3qP_06hTBRc");

            TemplateMsg.Data sendToPassengerData = new TemplateMsg.Data();
            sendToPassengerData.setFirst(new TemplateMsg.DataValue("您好，你的订单成功被接单"));
            sendToPassengerData.setKeyword1(new TemplateMsg.DataValue(order.getId()));
            sendToPassengerData.setKeyword2(new TemplateMsg.DataValue(dateFormat.format(order.getOrderTime())));
            sendToPassengerData.setKeyword3(new TemplateMsg.DataValue(driver.getTrueName()));
            sendToPassengerData.setKeyword4(new TemplateMsg.DataValue(driver.getPhoneNumber()));
            sendToPassengerData.setRemark(new TemplateMsg.DataValue("请等待司机联系或主动联系司机确认出发时间和地点"));
            sendToPassengerMsg.setData(sendToPassengerData);

            sendToPassengerMsg.setTouser(order.getPassenger().getWebOpenid());
            TemplateUtils.sendTemplateMsg(sendToPassengerMsg, WxUtils.fetchAccessToken().getAccess_token());


            return Msg.success("操作成功");

        } else {
            return Msg.failedDataMsg("id", "该订单不存在");
        }
    }
}


