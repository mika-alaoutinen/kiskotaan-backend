package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.errors.badrequest.BadRequestException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.ScoreCardMapper;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.repositories.CourseRepository;
import com.mika.kiskotaan.repositories.ScoreCardRepository;
import com.mika.kiskotaan.services.ScoreCardService;
import com.mika.kiskotaan.validators.NewResourceValidator;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.ScoreCardResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScoreCardServiceImpl implements ScoreCardService {
    private final ScoreCardMapper mapper;
    private final ScoreCardRepository repository;
    private final NewResourceValidator validator;

    @Override
    public ScoreCardResource getScoreCard(Long id) {
        return repository.findById(id)
                .map(mapper::toResource)
                .orElseThrow(() -> new NotFoundException(new ScoreCard(), id));
    }

    @Override
    public ScoreCardResource addScoreCard(final NewScoreCardResource resource) throws BadRequestException {
        final NewScoreCardResource validated = validator.validateNewResource(resource);
        ScoreCard newScoreCard = repository.save(mapper.toModel(resource));
        return mapper.toResource(newScoreCard);
    }

    @Override
    public void deleteScoreCard(Long id) {
        repository.deleteById(id);
    }
}
