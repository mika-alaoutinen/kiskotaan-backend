package com.mika.kiskotaan.dao;

import com.mika.kiskotaan.models.ScoreCard;

import java.util.Optional;

public interface ScoreCardDao {
    Optional<ScoreCard> getScoreCard(Long id);
    ScoreCard addScoreCard(ScoreCard newScoreCard);
    void deleteScoreCard(Long id);
}
