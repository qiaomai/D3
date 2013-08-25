package us.shareby.core.exception;

import java.util.HashMap;

/**
 * error response.
 *
 * @author chengdong
 */
public class ErrorResponse extends HashMap<String, Object> {

    private static final long serialVersionUID = -3136268132189005176L;

    public ErrorResponse(ErrorCode errorCode) {
        this.put("code", errorCode.getStatusCode());
        this.put("message", errorCode.getMessage());
    }

    public ErrorResponse() {
    }

    public void setMessage(String message) {
        this.put("message", message);
    }

    public void setErrorCode(int errorCode) {
        this.put("code", errorCode);
    }

    public void setError(ErrorCode errorCode) {
        this.put("code", errorCode.getStatusCode());
        this.put("message", errorCode.getMessage());
    }

    public void setError(BaseRuntimeException e) {
        this.put("code", e.getStatusCode());
        this.put("message", e.getMessage());
    }

}
