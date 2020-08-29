package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.mappers.ScoreCardMapper;
import com.mika.kiskotaan.repositories.ScoreCardRepository;
import com.mika.kiskotaan.services.ScoreCardService;
import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.ScoreCardResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoreCardServiceImpl implements ScoreCardService {
    private final ScoreCardMapper mapper;
    private final ScoreCardRepository repository;

    @Override
    public ScoreCardResource getScoreCard(Long id) {
        return null;
    }

    @Override
    public ScoreCardResource addScoreCard(NewScoreCardResource resource) {
        return null;
    }

    @Override
    public ScoreCardResource editScoreCard(ScoreCardResource resource) {
        return null;
    }

    @Override
    public void deleteScoreCard(Long id) {

    }
}
