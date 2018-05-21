package com.wteam.wx.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wteam.wx.bean.AccessToken;
import com.wteam.wx.bean.WxAppletEncryption;
import com.wteam.wx.bean.WxAppletLoginInfo;
import com.wteam.wx.bean.WxAppletUserInfo;
import jodd.util.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;
import java.util.Properties;

public class WxUtils {

    private static final Logger log = LoggerFactory.getLogger(WxUtils.class);

    //临时保存AccessToken
    private static AccessToken accessToken = null;

    /**
     * 获取access_token
     *
     * @return
     */
    public static AccessToken fetchAccessToken() {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
        if (accessToken == null || System.currentTimeMillis() > (accessToken.getFetch_time()
                + Long.parseLong(accessToken.getExpires_in()) * 1000 - 1000 * 30)) {
            Properties p = new Properties();
            String jsonText;
            try {
                p.load(TemplateUtils.class.getClassLoader().getResourceAsStream("wx.properties"));
                url = url.replaceAll("APPID", p.getProperty("appid"));
                url = url.replaceAll("APPSECRET", p.getProperty("appsecret"));
                jsonText = SSLUtils.httpsGet(new URI(url));
                accessToken = new ObjectMapper().readValue(jsonText, AccessToken.class);
                accessToken.setFetch_time(System.currentTimeMillis());
            } catch (URISyntaxException | IOException e) {
                log.error("获取AccessToken失败：", e);
            }
        }
        return accessToken;
    }


    public static WxAppletUserInfo decryptData(WxAppletEncryption encryption, WxAppletLoginInfo loginInfo) {
        //加密密匙
        byte[] keyByte = Base64.decode(loginInfo.getSession_key());
        //偏移量
        byte[] ivByte = Base64.decode(encryption.getIv());
        //被加密的数据
        byte[] dataByte = Base64.decode(encryption.getEncryptedData());

        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                return new ObjectMapper().readValue(new String(resultByte, "UTF-8"), WxAppletUserInfo.class);
            }
            log.warn("解密出的小程序用户信息为空");
            return null;
        } catch (Exception e) {
            log.error("解密小程序用户信息失败", e);
            return null;
        }
    }

}
