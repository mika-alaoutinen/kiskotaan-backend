package com.mika.kiskotaan.errors.badrequest;

import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.models.Player;
import kiskotaan.openapi.model.NewScoreCardResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ScoreCardException extends RuntimeException {

    public ScoreCardException(Course course) {
        super("New score card contains a course that does not exist in database.");
    }

    public ScoreCardException(Player player) {
        super("New score card contains one or more players that do not exist in database.");
    }

    public ScoreCardException(NewScoreCardResource newScoreCardResource) {
        super("Unknown exception occurred when trying to save resource \n" + newScoreCardResource);
    }
}
