package ru.practicum.ewm.stats.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundExceptions(Exception e) {
        log.error(e.getMessage());
        return new ApiError(
                e.getClass().toString(),
                e.getStackTrace(),
                e.getMessage(),
                e.getCause(),
                HttpStatus.NOT_FOUND,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
            IllegalArgumentException.class,
            HttpMessageNotReadableException.class,
            MissingRequestHeaderException.class,
            MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestExceptions(Exception e) {
        log.error(e.getMessage());
        return new ApiError(
                e.getClass().toString(),
                e.getStackTrace(),
                e.getMessage(),
                e.getCause(),
                HttpStatus.BAD_REQUEST,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictExceptions(Exception e) {
        log.error(e.getMessage());
        return new ApiError(
                e.getClass().toString(),
                e.getStackTrace(),
                e.getMessage(),
                e.getCause(),
                HttpStatus.CONFLICT,
                LocalDateTime.now()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        log.error(e.getMessage());
        return new ApiError(
                e.getClass().toString(),
                e.getStackTrace(),
                e.getMessage(),
                e.getCause(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                LocalDateTime.now()
        );
    }

}
