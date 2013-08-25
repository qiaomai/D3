/**
 * Copyright (c) 2013 Conversant Solutions. All rights reserved.
 *
 * Created on Jun 3, 2013.
 */
package us.shareby.core.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import us.shareby.core.exception.BaseRuntimeException;
import us.shareby.core.exception.ErrorCode;
import us.shareby.core.exception.ErrorResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * TODO.
 *
 * @author jasonCheng
 */
public class ExceptionResolver implements HandlerExceptionResolver {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionResolver.class);

    public HttpMessageConverter getJacksonHttpMessageConverter() {
        return jacksonHttpMessageConverter;
    }

    public void setJacksonHttpMessageConverter(HttpMessageConverter jacksonHttpMessageConverter) {
        this.jacksonHttpMessageConverter = jacksonHttpMessageConverter;
    }

    private HttpMessageConverter jacksonHttpMessageConverter;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {
        try {
            return handleException(ex, request, response, handler);
        } catch (Exception handlerException) {
            logger.error("handleBaseRuntimeException error", handlerException);
        }
        return null;
    }

    private ModelAndView handleException(Exception ex, HttpServletRequest request, HttpServletResponse response,
                                         Object handler) throws IOException {
        logger.error("server error", ex);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        ErrorResponse returnValue = new ErrorResponse();
        if (ex instanceof BaseRuntimeException) {
            returnValue.setError((BaseRuntimeException) ex);
        } else {
            returnValue.setError(ErrorCode.ERROR_SERVER_INNER_ERROR);
        }
        String header = request.getHeader("Content-Type");
        if (header == null) {
            header = "";
        }
        boolean isJSON = header.startsWith("application/json");
        HttpOutputMessage outputMessage = new ServletServerHttpResponse(response);

        if (isJSON) {
            Class<?> returnValueType = returnValue.getClass();
            if (jacksonHttpMessageConverter != null) {
                jacksonHttpMessageConverter.write(returnValue, MediaType.APPLICATION_JSON, outputMessage);
            }
        }

        return new ModelAndView();
    }

}
