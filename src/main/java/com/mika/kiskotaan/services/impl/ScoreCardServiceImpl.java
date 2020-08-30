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
        ScoreCard edited = mapper.toModel(resource);

        return repository.findById(id)
                .map(scoreCard -> mapper.editModel(edited, scoreCard))
                .map(repository::save)
                .map(mapper::toResource)
                .orElseThrow(() -> new NotFoundException(edited, id));
    }

    @Override
    public void deleteScoreCard(Long id) {
        repository.deleteById(id);
    }
}
