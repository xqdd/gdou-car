package com.wteam.car.web.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CrossDomainInterceptor extends HandlerInterceptorAdapter {


    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "GET,POST,HEAD,PUT,DELETE,OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "range,Origin,Accept, X-Requested-With, Content-Type");


        //设置不缓存
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        return super.preHandle(request, response, handler);
    }
}
