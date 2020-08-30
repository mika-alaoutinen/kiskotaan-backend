package com.mika.kiskotaan.services;

import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.ScoreCardResource;

public interface ScoreCardService {
    ScoreCardResource getScoreCard(Long id);
    ScoreCardResource addScoreCard(NewScoreCardResource resource);
    ScoreCardResource editScoreCard(Long id, ScoreCardResource resource);
    void deleteScoreCard(Long id);
}
