package com.mika.kiskotaan.services;

import com.mika.kiskotaan.errors.badrequest.ScoreRowException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import kiskotaan.openapi.model.ScoreRowResource;

public interface ScoreService {
    ScoreRowResource editScoreRow(Long scoreCardId, ScoreRowResource resource)
            throws NotFoundException, ScoreRowException;
}
