package com.wteam.wx.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wteam.wx.bean.AccessToken;
import com.wteam.wx.bean.TemplateMsg;
import org.apache.http.entity.StringEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

public class WxUtils {


    /**
     * 获取access_token
     *
     * @return
     */
    private static AccessToken fetchAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        Properties p = new Properties();
        try {
            p.load(WxUtils.class.getClassLoader().getResourceAsStream("wx.properties"));


            url = url.replaceAll("APPID", p.getProperty("appid"));
            url = url.replaceAll("APPSECRET", p.getProperty("appsecret"));


            String jsonText = SSLUtils.httpsGet(new URI(url));
            System.out.println(jsonText);

            return new ObjectMapper().readValue(jsonText, AccessToken.class);
        } catch (URISyntaxException | IOException e) {
            return null;
        }
    }


    /**
     * 设置模板行业信息
     *
     * @param access_token
     * @return
     */
    public static String setIndustry(String access_token) {
        String url = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";
        try {

            url = url.replaceAll("ACCESS_TOKEN", access_token);


            JSONObject params = new JSONObject();

            params.put("industry_id1", "16");
            params.put("industry_id2", "38");

            System.out.println(params.toJSONString());


            String jsonText = SSLUtils.httpsPost(new URI(url), new StringEntity(params.toString(), "utf-8"));
            System.out.println(jsonText);
            return jsonText;
        } catch (URISyntaxException e) {
            return null;
        }
    }


    /**
     * 获取模板行业信息
     *
     * @param access_token
     * @return
     */
    public static String getIndustry(String access_token) {
        String url = "https://api.weixin.qq.com/cgi-bin/template/get_industry?access_token=ACCESS_TOKEN";
        try {
            url = url.replaceAll("ACCESS_TOKEN", access_token);
            String jsonText = SSLUtils.httpsGet(new URI(url));
            System.out.println(jsonText);
            return jsonText;
        } catch (URISyntaxException e) {
            return null;
        }
    }

    //发送模板消息
    public static String sendTemplateMsg(String access_token, TemplateMsg templateMsg) {

        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
        try {
            url = url.replaceAll("ACCESS_TOKEN", access_token);
            String jsonText = SSLUtils.httpsPost(new URI(url), new StringEntity(JSON.toJSONString(templateMsg), "utf-8"));
            System.out.println(jsonText);
            return jsonText;
        } catch (URISyntaxException e) {
            return null;
        }
    }


    //获取access_token
    public static String getAccessToken() {
        Properties p = new Properties();
        try {
            File file = new File(WxUtils.class.getClassLoader().getResource("").getFile() + "runtime.properties");
            if (file.exists()) {
                p.load(new FileInputStream(file));
                if ((System.currentTimeMillis() - Long.parseLong(p.getProperty("fetch_time"))) / 1000
                        < Long.parseLong(p.getProperty("expires_in")) - 200)
                    return p.getProperty("access_token");
            }
            AccessToken token = fetchAccessToken();
            if (token != null) {
                p.setProperty("access_token", token.getAccess_token());
                p.setProperty("expires_in", token.getExpires_in());
                p.setProperty("fetch_time", System.currentTimeMillis() + "");
                p.store(new FileOutputStream(file), "runtime.properties");

                return token.getAccess_token();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
