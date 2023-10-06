package ru.practicum.ewm.stats.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({
            EntityNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundExceptions(Exception e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage(), e.getClass().toString());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage(), e.getClass().toString());
    }
}
