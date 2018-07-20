package io.renren.modules.hxy.entity;

import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @author Songxiaoyan
 * @since 2.0.0 2018/7/18
 */
@TableName("sz_sysnums")
public class SzSysnumsEntity {

    private String userId;

    private int keepnum;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getKeepnum() {
        return keepnum;
    }

    public void setKeepnum(int keepnum) {
        this.keepnum = keepnum;
    }
}
