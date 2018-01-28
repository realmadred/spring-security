package com.feng.security.validate;

import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 验证码
 */
public class ImageCode implements Serializable{

    private static final long serialVersionUID = -4527291425839704178L;

    private static final int DEFAULT_TIMEOUT = 60; // 默认过期时间60s
    private BufferedImage image; // 图片
    private String code; // 验证码
    private long expire; // 过期时间


    public ImageCode() {
    }

    public ImageCode(BufferedImage image, String code) {
        this.image = image;
        this.code = code;
        setTimeOut(DEFAULT_TIMEOUT);
    }

    public ImageCode setTimeOut(int seconds){
        setExpire(System.currentTimeMillis()+ TimeUnit.SECONDS.toMillis(seconds));
        return this;
    }


    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }
}
