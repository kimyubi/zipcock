package com.umc.zipcock.controller;

import com.umc.zipcock.error.UserNotFoundException;
import com.umc.zipcock.model.dto.DefaultRes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionAdvisor {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DefaultRes<String>> processValidationError(MethodArgumentNotValidException exception) {

        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder builder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: [");
            builder.append(fieldError.getRejectedValue());
            builder.append("]");
        }

        return new ResponseEntity<>(DefaultRes.response(HttpStatus.BAD_REQUEST.value(),
                "유효성 검사 에러", builder.toString()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<DefaultRes<String>> processUserNotFoundError(UserNotFoundException exception) {
        return new ResponseEntity<>(DefaultRes.response(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    }
