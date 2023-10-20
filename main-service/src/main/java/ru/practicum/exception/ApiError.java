package ru.practicum.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ApiError {

    private final String error;

    private final StackTraceElement[] stackTrace;

    private final String message;

    private final Throwable reason;

    private final HttpStatus status;

    private final LocalDateTime localDateTime;

}
