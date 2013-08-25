package us.shareby.core.exception;

/**
 * User: chengdong
 * Date: 13-8-25
 */
public enum ErrorCode {

    WARN_USERNAME_OR_PASSWORD_WRONG(101001, "username or password wrong!"),
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
