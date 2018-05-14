package com.wteam.car.web.exception;

import com.wteam.car.bean.interact.response.Msg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyExceptionHandlers {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    //普通参数校验错误,json参数校验错误
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public Object validExceptionHandler(Exception e) {
        BindingResult result;
        if (e instanceof BindException) {
            result = ((BindException) e).getBindingResult();
        } else {
            result = ((MethodArgumentNotValidException) e).getBindingResult();
        }
        Map<String, String> errors = new HashMap<>();
        result.getFieldErrors().forEach((fe) -> errors.put(fe.getField(), fe.getDefaultMessage()));
        return Msg.failed(errors);
    }


    //没session
    @ExceptionHandler({ServletRequestBindingException.class})
    public Object sessionMiss(ServletRequestBindingException e, HttpServletRequest request) {
        return Msg.failedAndDebug("请先登录", e.getMessage());
    }

    //数字格式化错误
    @ExceptionHandler(NumberFormatException.class)
    public Msg numberFormatExceptionHandler(NumberFormatException e) {
        return Msg.failed(e.getLocalizedMessage());
    }


    //   上传错误
    @ExceptionHandler(MultipartException.class)
    public void multipartExceptionHandler(MultipartException e) {
        log.error("发生上传错误", e);
    }


    //格式转化错误
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Msg converterError(HttpMessageNotReadableException e) {
        return Msg.failedAndDebug("参数有误", e.getMessage());
    }

    //    上传限制错误
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public void maxUploadSizeExceededExceptionHandler(MaxUploadSizeExceededException e) {
        log.error("发生上传限制错误", e);
    }
}
