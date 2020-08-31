package com.mika.kiskotaan.services;

import kiskotaan.openapi.model.ScoreRowResource;

public interface ScoreService {
    ScoreRowResource editScoreRow(Long scoreCardId, ScoreRowResource resource);
}
