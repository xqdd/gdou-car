package com.wteam.wx.utils;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


public class SSLUtils {
    /**
     * 创建ssl连接
     *
     * @return
     */
    public static CloseableHttpClient createSSLClientDefault() {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                //信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    public static String httpsGet(URI uri) {
        CloseableHttpClient httpClient = SSLUtils.createSSLClientDefault();
        try {
            HttpResponse response = httpClient.execute(new HttpGet(uri));
            return new String(EntityUtils.toString(response.getEntity()).getBytes("iso8859-1"), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String httpsPost(URI uri, HttpEntity entity) {
        CloseableHttpClient httpClient = SSLUtils.createSSLClientDefault();
        try {
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(entity);
            HttpResponse response = httpClient.execute(httpPost);
            return new String(EntityUtils.toString(response.getEntity()).getBytes("iso8859-1"), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}