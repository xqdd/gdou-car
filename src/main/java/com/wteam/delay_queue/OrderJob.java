package com.wteam.delay_queue;

public class OrderJob {

    public static enum Type {
        ORDER_CANCEL_TIME_OUT,
        ORDER_COMPLETE_TIME_OUT,
    }

    //订单处理类型，0订单自动过期 1订单自动完成
    private Integer type;
    //订单id
    private String id;
    //延迟时间点（时间戳）
    private Long delayTime;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getDelayTime() {
        return delayTime;
    }

    public void setDelayTime(Long delayTime) {
        this.delayTime = delayTime;
    }
}
