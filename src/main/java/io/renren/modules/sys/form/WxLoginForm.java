package io.renren.modules.sys.form;

import org.apache.commons.lang.StringUtils;

/**
 * 微信端登录表单，检查是否绑定用
 *
 * @author Songxiaoyan
 * @since 2.0.0 2018/4/27
 */
public class WxLoginForm {

    private String username;

    private String password;

    private int userOrCustomer;

    private String openId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserOrCustomer() {
        return userOrCustomer;
    }

    public void setUserOrCustomer(int userOrCustomer) {
        this.userOrCustomer = userOrCustomer;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
