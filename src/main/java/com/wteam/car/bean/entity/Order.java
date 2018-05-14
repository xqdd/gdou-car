package com.wteam.car.bean.entity;

import com.wteam.car.utils.jsonview.OrderGroup;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;


@Entity
@Table(name = "`order`")
public class Order {

    @Id
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @GeneratedValue(generator = "uuid")
    @NotEmpty(groups = {OrderGroup.id.class, OrderGroup.DriverOrder.takeOrder.class})
    private String id;

    //级联更新：指A类新增或者变化，会级联B对象（新增或者变化）
    @ManyToOne(cascade = CascadeType.MERGE)
    @Valid
    @NotNull(groups = {OrderGroup.PassengerOrder.add.class})
    private User passenger;

    @ManyToOne
    @Valid
    @NotNull(groups = {OrderGroup.DriverOrder.takeOrder.class})
    private User driver;

    //出发地
    @NotEmpty(groups = {OrderGroup.PassengerOrder.add.class})
    private String departure;

    //目的地
    @NotEmpty(groups = {OrderGroup.PassengerOrder.add.class})
    private String destination;

    @Min(value = 0, groups = {OrderGroup.PassengerOrder.add.class})
    private BigDecimal price;

    //预约时间
    @NotNull(groups = {OrderGroup.PassengerOrder.add.class})
    @Future(groups = {OrderGroup.PassengerOrder.add.class})
    private Timestamp appointmentTime;

    //有效时长
    @Min(value = 0, groups = {OrderGroup.PassengerOrder.add.class})
    @NotNull(groups = {OrderGroup.PassengerOrder.add.class})
    private Integer validTime;

    //备注
    private String ps;

    private Timestamp createTime;

    private Timestamp cancelTime;

    private Timestamp completeTime;

    //失效时间
    private Timestamp invalidedTime;

    //接单时间
    private Timestamp orderTime;

    //状态,0等待接单，1被接单，2订单完成，3订单取消，4订单超时，5订单已重置
    private Integer status;


    public Timestamp getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(Timestamp appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getPassenger() {
        return passenger;
    }

    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }


    public Integer getValidTime() {
        return validTime;
    }

    public void setValidTime(Integer validTime) {
        this.validTime = validTime;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(Timestamp cancelTime) {
        this.cancelTime = cancelTime;
    }

    public Timestamp getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Timestamp completeTime) {
        this.completeTime = completeTime;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getStatus() {
        return status;
    }

    //状态,0等待接单，1被接单，2订单完成，3订单取消，4订单超时，5订单已重置
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getInvalidedTime() {
        return invalidedTime;
    }

    public void setInvalidedTime(Timestamp invalidedTime) {
        this.invalidedTime = invalidedTime;
    }


    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
