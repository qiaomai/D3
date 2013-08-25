package us.shareby.core.exception;

/**
 * @author chengdong
 */
public class BaseRuntimeException extends RuntimeException {
    /**
     * status code
     */
    private int statusCode;


    /**
     * constructor with status code and object
     *
     * @param statusCode status code
     */
    public BaseRuntimeException(int statusCode) {
        super();
    }

    public BaseRuntimeException(ErrorCode err) {
        super(err.getMessage());
        this.statusCode = err.getStatusCode();
    }

    public BaseRuntimeException(ErrorCode err, Throwable t) {
        super(err.getMessage(), t);
        this.statusCode = err.getStatusCode();
    }

    /**
     * constructor with fileds statusCode and message
     *
     * @param statusCode status code
     * @param message    message
     */
    public BaseRuntimeException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }


    /**
     * constructor with fileds statusCode and message
     *
     * @param statusCode status code
     * @param message    message
     */
    public BaseRuntimeException(int statusCode, String message, Throwable t) {
        super(message, t);
        this.statusCode = statusCode;
    }

    /**
     * constructor with status code and object
     *
     * @param statusCode status code
     * @param object     Object
     */
    public BaseRuntimeException(int statusCode, Throwable object) {
        super(object);
        this.statusCode = statusCode;
    }

    /**
     * get status code
     *
     * @return status code
     */
    public int getStatusCode() {
        return statusCode;
    }
}
