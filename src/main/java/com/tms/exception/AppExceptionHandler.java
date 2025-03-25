package com.tms.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = AgeException.class)
    public String ageExceptionHandler(AgeException exception, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        return exception.getMessage();
    }

    @ExceptionHandler(value = Exception.class)
    public String allExceptionsHandler(Exception exception, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CONFLICT);
        return exception.getMessage();
    }
}