package com.mika.kiskotaan.errors.badrequest;

import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.PlayerResource;
import kiskotaan.openapi.model.ScoreRowResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    public BadRequestException(CourseResource courseResource) {
        super("New score card contains a course that does not exist in database.");
    }

    public BadRequestException(PlayerResource playerResource) {
        super("New score card contains one or more players that do not exist in database.");
    }

    public BadRequestException(ScoreRowResource row) {
        super("Could not edit score row with hole number " + row.getHole());
    }
}
