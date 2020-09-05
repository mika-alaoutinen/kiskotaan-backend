package com.mika.kiskotaan.errors.badrequest;

import kiskotaan.openapi.model.ScoreRowResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ScoreRowException extends RuntimeException {

    public ScoreRowException(ScoreRowResource row) {
        super("Could not edit score row with hole number " + row.getHole());
    }
}
