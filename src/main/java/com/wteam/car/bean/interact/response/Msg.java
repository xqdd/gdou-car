package com.wteam.car.bean.interact.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
public class Msg {


    public static Integer CODE_FAILED = 0;
    public static Integer CODE_SUCCESS = 1;

    private Integer code;
    private Object data;
    private String msg;
    private String debugMsg;


    public Msg(String msg) {
        this.msg = msg;
    }


    public Msg() {
    }

    public Msg(Integer code, Object data) {
        this.data = data;
        this.code = code;
    }

    public Msg(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Msg(Integer code, String msg, String debugMsg) {
        this.code = code;
        this.msg = msg;
        this.debugMsg = debugMsg;
    }

    private Msg(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Msg failedDetail(String msg, Object errors) {
        return new Msg(CODE_FAILED, msg, errors);
    }

    public String getDebugMsg() {
        return debugMsg;
    }

    public void setDebugMsg(String debugMsg) {
        this.debugMsg = debugMsg;
    }


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

    public static Msg failedAndDebug(String msg, String debugMsg) {
        return new Msg(CODE_FAILED, msg, debugMsg);
    }


    public static Msg successDataMsg(String... data) {
        return new Msg(CODE_SUCCESS, data2Map(data));
    }


    public static Msg failedDataMsg(String... data) {
        return new Msg(CODE_FAILED, data2Map(data));
    }



    public static Msg successData(Object data) {
        return new Msg(CODE_SUCCESS, data);
    }

    public static Msg failed(Object data) {
        return new Msg(CODE_FAILED, data);
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
