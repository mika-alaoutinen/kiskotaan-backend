package com.mika.kiskotaan.errors.notfound;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

public class NotFoundAdvice {

    @ResponseBody
    @ExceptionHandler
    public String NotFoundHandler(NotFoundException e) {
        return e.getMessage();
    }
}
