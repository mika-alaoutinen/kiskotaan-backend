package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.ScoreCardMapper;
import com.mika.kiskotaan.models.ScoreCard;
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
        return repository.findById(id)
                .map(mapper::toResource)
                .orElseThrow(() -> new NotFoundException(new ScoreCard(), id));
    }

    @Override
    public ScoreCardResource addScoreCard(NewScoreCardResource resource) {
        ScoreCard newScoreCard = repository.save(mapper.toModel(resource));
        return mapper.toResource(newScoreCard);
    }

    @Override
    public ScoreCardResource editScoreCard(Long id, ScoreCardResource resource) {
        ScoreCard existingCard = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(new ScoreCard(), id));

        // Updates existingCard with values from resource
        mapper.editModel(mapper.toModel(resource), existingCard);
        ScoreCard saved = repository.save(existingCard);
        return mapper.toResource(saved);
    }

    @Override
    public void deleteScoreCard(Long id) {
        repository.deleteById(id);
    }
}
