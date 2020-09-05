package com.mika.kiskotaan.services;

import com.mika.kiskotaan.errors.badrequest.PlayerException;
import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.ScoreCardResource;

public interface ScoreCardService {
    ScoreCardResource getScoreCard(Long id);
    ScoreCardResource addScoreCard(NewScoreCardResource resource) throws PlayerException;
    void deleteScoreCard(Long id);
}
