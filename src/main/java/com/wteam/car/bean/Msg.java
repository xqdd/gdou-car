package com.wteam.car.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
public class Msg {


    public static Integer CODE_FAILED = 0;
    public static Integer CODE_SUCCESS = 1;

    public Msg(String msg) {
        this.msg = msg;
    }


    public Msg() {
    }

    public Msg(Integer code, Object data) {
        this.data = data;
        this.code = code;
    }


    private Msg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    private Integer code;
    private Object data;
    private String msg;

    public static Msg success(String msg) {
        return new Msg(CODE_SUCCESS, msg);
    }


    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


    public static Msg failed(String msg) {
        return new Msg(CODE_FAILED, msg);
    }


    public static Msg successData(String... data) {
        return new Msg(CODE_SUCCESS, data2Map(data));
    }

    public static Msg failed(Object data) {
        return new Msg(CODE_FAILED, data);
    }

    public static Msg failedData(String... data) {
        return new Msg(CODE_FAILED, data2Map(data));
    }


    private static Map<String, String> data2Map(String[] arguments) {
        int length = arguments.length;
        Assert.isTrue(length % 2 == 0, "传入的参数个数必须为偶数");
        HashMap<String, String> map = new HashMap<>();

        for (int i = 0; i < length / 2; ++i) {
            map.put(arguments[i], arguments[i + length / 2]);
        }
        return map;
    }
}
