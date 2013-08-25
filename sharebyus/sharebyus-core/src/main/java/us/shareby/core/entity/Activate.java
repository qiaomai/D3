package us.shareby.core.entity;

import java.util.Date;

/**
 * User: chengdong
 * Date: 13-8-25
 */
public class Activate {
    public String getActivateCode() {
        return activateCode;
    }

    public void setActivateCode(String activateCode) {
        this.activateCode = activateCode;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    private String activateCode;
    private long userId;
    private Date createTime;


}
