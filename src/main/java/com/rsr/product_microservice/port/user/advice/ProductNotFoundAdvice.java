package com.rsr.product_microservice.port.user.advice;

import com.rsr.product_microservice.port.user.exceptions.NoProductsException;
import com.rsr.product_microservice.port.user.exceptions.UnknownProductIdException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ProductNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(value = UnknownProductIdException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String unknownProductIdHandler(UnknownProductIdException e){
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(value = IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String illegalArgumentHandler(IllegalArgumentException e){
        return e.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(value = NoProductsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String noProductsHandler(NoProductsException e){
        return e.getMessage();
    }
}
