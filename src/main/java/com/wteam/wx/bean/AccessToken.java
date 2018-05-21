package com.wteam.wx.bean;

public class AccessToken {

    private String access_token;

    private String expires_in;

    //获取时间
    private Long fetch_time;


    public Long getFetch_time() {
        return fetch_time;
    }

    public void setFetch_time(Long fetch_time) {
        this.fetch_time = fetch_time;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(String expires_in) {
        this.expires_in = expires_in;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "access_token='" + access_token + '\'' +
                ", expires_in='" + expires_in + '\'' +
                '}';
    }
}
