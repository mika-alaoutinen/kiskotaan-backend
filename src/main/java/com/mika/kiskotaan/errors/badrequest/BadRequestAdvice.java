package com.mika.kiskotaan.errors.badrequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class BadRequestAdvice {

    @ResponseBody
    @ExceptionHandler
    public String BadRequestHandler(BadRequestException e) {
        return e.getMessage();
    }
}
