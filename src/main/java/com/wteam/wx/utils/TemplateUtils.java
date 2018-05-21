package com.wteam.wx.utils;

import com.alibaba.fastjson.JSON;
import com.wteam.wx.bean.TemplateMsg;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;

public class TemplateUtils {


    private static final Logger log = LoggerFactory.getLogger(TemplateUtils.class);


    //发送模板消息
    public static String sendTemplateMsg(TemplateMsg templateMsg, String access_token) {

        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
        try {
            url = url.replaceAll("ACCESS_TOKEN", access_token);
            String jsonText = SSLUtils.httpsPost(new URI(url), new StringEntity(JSON.toJSONString(templateMsg), "utf-8"));
            System.out.println(jsonText);
            return jsonText;
        } catch (URISyntaxException e) {
            log.error("发送模板消息失败", e);
            return null;
        }
    }


}
