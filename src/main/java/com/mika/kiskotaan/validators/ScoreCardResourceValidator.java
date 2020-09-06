package com.mika.kiskotaan.validators;

import com.mika.kiskotaan.errors.badrequest.ScoreCardException;
import kiskotaan.openapi.model.NewScoreCardResource;

public interface ScoreCardResourceValidator {
    NewScoreCardResource validateNewResource(NewScoreCardResource resource) throws ScoreCardException;
}
