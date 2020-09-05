package com.mika.kiskotaan.errors.badrequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ResponseBody
public class BadRequestAdvice {

    @ExceptionHandler
    public String BadRequestHandler(PlayerException e) {
        return e.getMessage();
    }

    @ExceptionHandler
    public String BadRequestHandler(ScoreCardException e) {
        return e.getMessage();
    }

    @ExceptionHandler
    public String BadRequestHandler(ScoreRowException e) {
        return e.getMessage();
    }
}
