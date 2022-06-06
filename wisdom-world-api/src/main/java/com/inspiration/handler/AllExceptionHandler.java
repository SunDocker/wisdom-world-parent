package com.inspiration.handler;

import com.inspiration.vo.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author SunDocker
 */
@RestControllerAdvice
public class AllExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result doException(Exception e) throws Exception {
        if (e instanceof AuthenticationException) {
            throw e;
        }
        e.printStackTrace();
        return Result.fail(501, e.getMessage());
    }
}
