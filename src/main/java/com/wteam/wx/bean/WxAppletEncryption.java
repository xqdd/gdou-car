package com.wteam.wx.bean;

import javax.validation.constraints.NotEmpty;

public class WxAppletEncryption {
    @NotEmpty
    private String iv;

    @NotEmpty
    private String encryptedData;


    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }
}
