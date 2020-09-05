package com.mika.kiskotaan.services;

import com.mika.kiskotaan.errors.badrequest.BadRequestException;
import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.ScoreCardResource;

public interface ScoreCardService {
    ScoreCardResource getScoreCard(Long id);
    ScoreCardResource addScoreCard(NewScoreCardResource resource) throws BadRequestException;
    void deleteScoreCard(Long id);
}
