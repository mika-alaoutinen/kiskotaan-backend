package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.errors.badrequest.ScoreCardException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.MapperUtils;
import com.mika.kiskotaan.mappers.ScoreCardMapper;
import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.repositories.ScoreCardRepository;
import com.mika.kiskotaan.services.CourseService;
import com.mika.kiskotaan.services.PlayerService;
import com.mika.kiskotaan.services.ScoreCardService;
import com.mika.kiskotaan.validators.ScoreCardResourceValidator;
import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.ScoreCardResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ScoreCardServiceImpl implements ScoreCardService {
    private final CourseService courseService;
    private final PlayerService playerService;
    private final ScoreCardMapper mapper;
    private final ScoreCardRepository repository;
    private final ScoreCardResourceValidator validator;

    @Override
    public ScoreCardResource getScoreCard(Long id) throws NotFoundException {
        return repository.findById(id)
                .map(mapper::toResource)
                .orElseThrow(() -> new NotFoundException(new ScoreCard(), id));
    }

    public ScoreCardResource addScoreCard(final NewScoreCardResource newScoreCard) throws ScoreCardException {
        return Stream.of(validator.validateNewResource(newScoreCard))
                .map(this::createScoreCard)
                .map(repository::save)
                .map(mapper::toResource)
                .findAny()
                .orElseThrow(() -> new ScoreCardException(newScoreCard));
    }

    @Override
    public void deleteScoreCard(Long id) {
        repository.deleteById(id);
    }

    private ScoreCard createScoreCard(NewScoreCardResource resource) {
        Course course = courseService.getCourse(resource.getCourseId().longValue());
        var playerIds = MapperUtils.mapIds(resource.getPlayersIds());
        var players = playerService.getPlayers(playerIds);
        return mapper.toScoreCard(course, players);
    }
}