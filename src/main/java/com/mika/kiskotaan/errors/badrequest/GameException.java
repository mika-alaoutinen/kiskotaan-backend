package com.mika.kiskotaan.errors.badrequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GameException extends RuntimeException {

    public GameException(Long scoreCardId) {
        super(String.format("Could not start new game with given score card ID: %s", scoreCardId));
    }
}
