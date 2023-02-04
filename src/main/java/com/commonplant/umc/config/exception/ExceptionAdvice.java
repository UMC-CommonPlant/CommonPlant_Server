package com.commonplant.umc.config.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;




@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {


    //400
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> BadRequestException(BadRequestException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getStatus()));
    }






}
