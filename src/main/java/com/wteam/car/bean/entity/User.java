package com.wteam.car.bean.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wteam.car.utils.jsonview.OrderGroup;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
public class User {


    @Id
    @NotEmpty(groups = {OrderGroup.miniAppSaveUser.class, OrderGroup.id.class})
    private String unionId;


    @JsonIgnore
    private String webOpenid;

    @JsonIgnore
    private String appletOpenid;

    @NotEmpty(groups = {OrderGroup.miniAppSaveUser.class})
    private String nickName;
    @NotEmpty(groups = {OrderGroup.PassengerOrder.add.class, OrderGroup.DriverOrder.takeOrder.class})
    private String trueName;

    @NotEmpty(groups = {OrderGroup.PassengerOrder.add.class, OrderGroup.DriverOrder.takeOrder.class})
    private String phoneNumber;

    @NotEmpty(groups = {OrderGroup.miniAppSaveUser.class})
    private String headimgurl;

    @NotEmpty(groups = {OrderGroup.DriverOrder.takeOrder.class})
    //用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
    private String sex;


    //作为乘客拥有的订单
    @OneToMany(mappedBy = "passenger")
    @JsonIgnore
    private List<Order> passengerOrder;


    //作为司机拥有的订单
    @OneToMany(mappedBy = "driver")
    @JsonIgnore
    private List<Order> driverOrders;


    public String getWebOpenid() {
        return webOpenid;
    }

    public void setWebOpenid(String webOpenid) {
        this.webOpenid = webOpenid;
    }

    public String getAppletOpenid() {
        return appletOpenid;
    }

    public void setAppletOpenid(String appletOpenid) {
        this.appletOpenid = appletOpenid;
    }

    public List<Order> getPassengerOrder() {
        return passengerOrder;
    }

    public void setPassengerOrder(List<Order> passengerOrder) {
        this.passengerOrder = passengerOrder;
    }

    public List<Order> getDriverOrders() {
        return driverOrders;
    }

    public void setDriverOrders(List<Order> driverOrders) {
        this.driverOrders = driverOrders;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public User() {
    }

    public User(String unionId, String sex) {
        this.unionId = unionId;
        this.sex = sex;
    }
}
