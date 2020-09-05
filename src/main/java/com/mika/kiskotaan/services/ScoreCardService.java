package com.mika.kiskotaan.services;

import com.mika.kiskotaan.errors.badrequest.ScoreCardException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.ScoreCardResource;

public interface ScoreCardService {
    ScoreCardResource getScoreCard(Long id) throws NotFoundException;
    ScoreCardResource addScoreCard(NewScoreCardResource resource) throws ScoreCardException;
    void deleteScoreCard(Long id);
}
