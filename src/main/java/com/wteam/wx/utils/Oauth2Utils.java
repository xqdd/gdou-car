package com.wteam.wx.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wteam.wx.bean.WxAppletLoginInfo;
import com.wteam.wx.bean.WxToken;
import com.wteam.wx.bean.WxUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Properties;

public class Oauth2Utils {


    private static final Logger log = LoggerFactory.getLogger(Oauth2Utils.class);

    /**
     * 生成微信网页登录授权链接
     *
     * @param callbackUrl
     * @return
     */
    public static String generateUrl(String callbackUrl) {
        Properties p = new Properties();
        try {
            p.load(Oauth2Utils.class.getClassLoader().getResourceAsStream("wx.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        url = url.replaceAll("APPID", p.getProperty("appid"));
        try {
            url = url.replaceAll("REDIRECT_URI",
                    URLEncoder.encode(p.getProperty("web_redirect_uri"), "utf-8"));
            url = url.replaceAll("STATE", URLEncoder.encode(StringUtils.isBlank(callbackUrl) ? "" : callbackUrl, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("生成跳转url失败：" + url, e);
        }
        return url;
    }

    /**
     * 使用code兑换access_token
     *
     * @param code
     * @return
     */
    public static WxToken get_access_token(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
        Properties p = new Properties();
        String jsonText = "";
        try {
            p.load(Oauth2Utils.class.getClassLoader().getResourceAsStream("wx.properties"));
            url = url.replaceAll("APPID", p.getProperty("appid"));
            url = url.replaceAll("SECRET", p.getProperty("appsecret"));
            url = url.replaceAll("CODE", code);

            jsonText = SSLUtils.httpsGet(new URI(url));

            return new ObjectMapper().readValue(jsonText, WxToken.class);
        } catch (URISyntaxException | IOException e) {
            log.error("获取登录access_token失败：" + jsonText, e);
            return null;
        }
    }


    /**
     * 使用access_token和open_id拉取用户信息
     *
     * @param wxToken
     * @return
     */
    public static WxUser get_wx_user(WxToken wxToken) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        String jsonText = "";
        try {
            url = url.replaceAll("ACCESS_TOKEN", wxToken.getAccess_token());
            url = url.replaceAll("OPENID", wxToken.getOpenid());

            jsonText = SSLUtils.httpsGet(new URI(url));
            return new ObjectMapper().readValue(jsonText, WxUser.class);
        } catch (URISyntaxException | IOException e) {
            log.error("获取用户信息失败：" + jsonText, e);
        }
        return null;
    }


    /**
     * 用code获取小程序登录信息
     * @param code
     * @return
     */
    public static Object login_mini_app(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
        String jsonText = "";
        Properties p = new Properties();
        try {
            p.load(Oauth2Utils.class.getClassLoader().getResourceAsStream("wx.properties"));
            url = url.replaceAll("APPID", p.getProperty("mini_appid"));
            url = url.replaceAll("SECRET", p.getProperty("mini_appsecret"));
            url = url.replaceAll("JSCODE", code);
            jsonText = SSLUtils.httpsGet(new URI(url));
            return new ObjectMapper().readValue(jsonText, WxAppletLoginInfo.class);
        } catch (URISyntaxException | IOException e) {
            log.error("登录小程序失败：" + jsonText, e);
            return jsonText;
        }
    }


}


