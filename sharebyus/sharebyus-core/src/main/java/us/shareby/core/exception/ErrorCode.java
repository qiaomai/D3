package us.shareby.core.exception;

/**
 * User: chengdong
 * Date: 13-8-25
 */
public enum ErrorCode {

    WARN_USERNAME_OR_PASSWORD_WRONG(101001, "username or password wrong!"),
    WARN_NAME_OR_EMAIL_USED(101002,"username or email is used"),
    WARN_INVALIDATE_ACTIVATE_CODE(101003,"invalidate activate code"),
    WARN_REQUIRED_PARAMS_NULL(101400,"params is not null"),

    ERROR_SERVER_INNER_ERROR(101005, "server inner error.");

    private int statusCode;

    private String message;

    private ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return this.message;
    }
}
