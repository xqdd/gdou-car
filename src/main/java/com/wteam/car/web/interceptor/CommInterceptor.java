package com.wteam.car.web.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wteam.car.bean.interact.response.Msg;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.servlet.ServletRequestContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CommInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request != null && ServletFileUpload.isMultipartContent(request)) {
            long maxSize = 20 * 1024 * 1024;
            if (new ServletRequestContext(request).contentLength() > maxSize) {
                response.setContentType("application/json; charset=utf-8");
                response.getWriter().print((new ObjectMapper()).writeValueAsString(Msg.failed("请先登录")));
                return false;
            }
        }
        return true;
    }
}
