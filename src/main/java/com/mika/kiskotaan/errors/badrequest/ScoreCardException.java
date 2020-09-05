package com.mika.kiskotaan.errors.badrequest;

import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.PlayerResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ScoreCardException extends RuntimeException {

    public ScoreCardException(CourseResource courseResource) {
        super("New score card contains a course that does not exist in database.");
    }

    public ScoreCardException(PlayerResource playerResource) {
        super("New score card contains one or more players that do not exist in database.");
    }

    public ScoreCardException(NewScoreCardResource newScoreCardResource) {
        super("Unknown exception occurred when trying to save resource \n" + newScoreCardResource);
    }
}
