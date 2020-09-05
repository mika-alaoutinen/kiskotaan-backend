package com.mika.kiskotaan.errors.badrequest;

import kiskotaan.openapi.model.NewPlayerResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PlayerException extends RuntimeException {

    public PlayerException(NewPlayerResource newPlayerResource) {
        super("Could not save resource \n" + newPlayerResource);
    }
}
