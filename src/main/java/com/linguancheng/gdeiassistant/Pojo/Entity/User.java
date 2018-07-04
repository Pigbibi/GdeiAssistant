package com.gdeiassistant.gdeiassistant.Pojo.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gdeiassistant.gdeiassistant.Tools.StringEncryptUtils;
import com.gdeiassistant.gdeiassistant.ValidGroup.User.ServiceQueryValidGroup;
import com.gdeiassistant.gdeiassistant.ValidGroup.User.UserLoginValidGroup;
import com.taobao.wsgsvr.WsgException;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Created by gdeiassistant on 2017/7/16.
 */

@Component
@Scope("prototype")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {

    @NotBlank(message = "用户名不能为空", groups = {UserLoginValidGroup.class, ServiceQueryValidGroup.class})
    @Size(min = 1, max = 20, message = "用户名长度超过限制", groups = {UserLoginValidGroup.class, ServiceQueryValidGroup.class})
    //教务系统账号用户名
    private String username;

    @NotBlank(message = "密码不能为空", groups = {UserLoginValidGroup.class})
    @Size(min = 1, max = 35, message = "密码长度超过限制", groups = {UserLoginValidGroup.class})
    //教务系统账号密码
    private String password;

    //教务系统账号加密Key值
    @NotBlank(message = "加密Key不能为空", groups = {ServiceQueryValidGroup.class})
    @Size(min = 32, max = 32, message = "加密Key长度不合法", groups = {ServiceQueryValidGroup.class})
    private String keycode;

    //教务系统账号学号
    @NotBlank(message = "学号不能为空", groups = {ServiceQueryValidGroup.class})
    @Size(min = 11, max = 11, message = "学号长度不合法", groups = {ServiceQueryValidGroup.class})
    private String number;

    private String xm;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKeycode() {
        return keycode;
    }

    public void setKeycode(String keycode) {
        this.keycode = keycode;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getXm() {
        return xm;
    }

    public void setXm(String xm) {
        this.xm = xm;
    }

    public User decryptUser() {
        User decryptUser = new User();
        try {
            if (username != null) {
                decryptUser.setUsername(StringEncryptUtils.decryptString(username));
            }
            if (password != null) {
                decryptUser.setPassword(StringEncryptUtils.decryptString(password));
            }
            if (keycode != null) {
                decryptUser.setKeycode(StringEncryptUtils.decryptString(keycode));
            }
            if (number != null) {
                decryptUser.setNumber(StringEncryptUtils.decryptString(number));
            }
        } catch (WsgException e) {
            e.printStackTrace();
        }
        return decryptUser;
    }

    public User encryptUser() {
        User encryptUser = new User();
        try {
            if (username != null) {
                encryptUser.setUsername(StringEncryptUtils.encryptString(username));
            }
            if (password != null) {
                encryptUser.setPassword(StringEncryptUtils.encryptString(password));
            }
            if (keycode != null) {
                encryptUser.setKeycode(StringEncryptUtils.encryptString(keycode));
            }
            if (number != null) {
                encryptUser.setNumber(StringEncryptUtils.encryptString(number));
            }
        } catch (WsgException e) {
            e.printStackTrace();
        }
        return encryptUser;
    }

    public User() {
        super();
    }

    public User(String username) {
        this.username = username;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, String keycode, String number) {
        this.username = username;
        this.password = password;
        this.keycode = keycode;
        this.number = number;
    }
}