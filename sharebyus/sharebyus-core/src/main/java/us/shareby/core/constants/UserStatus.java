package us.shareby.core.constants;

/**
 * User: chengdong
 * Date: 13-8-25
 * Time: 下午4:59
 */
public enum  UserStatus {
    register("register"),active("active"),disable("disable");

    private String status;

    private UserStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
