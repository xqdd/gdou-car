package com.wteam.car.bean.entity;

import com.wteam.car.utils.jsonview.OrderGroup;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Entity
public class User {


    @Id
    private String openid;

    private String nickName;

    @NotEmpty(groups = {OrderGroup.PassengerOrder.add.class})
    private String trueName;

    @NotEmpty(groups = {OrderGroup.PassengerOrder.add.class})
    private String phoneNumber;

    private String headimgurl;

    private String sex;


    //作为乘客拥有的订单
    @OneToMany(mappedBy = "passenger")
    private List<Order> passengerOrder;


    //作为司机拥有的订单
    @OneToMany(mappedBy = "driver")
    private List<Order> driverOrders;


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

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
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

    public User(String openid, String sex) {
        this.openid = openid;
        this.sex = sex;
    }
}
