package com.wteam.delay_queue;

public class OrderJob {
    //订单处理类型，0订单过期 1订单完成
    private Integer type;
    //订单id
    private Integer id;
    //延迟时间点（时间戳）
    private Long delayTime;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Long delayTime) {
        this.delayTime = delayTime;
    }
}
