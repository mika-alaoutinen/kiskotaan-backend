package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.errors.badrequest.ScoreCardException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
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

@Service
@RequiredArgsConstructor
public class ScoreCardServiceImpl implements ScoreCardService {
    private final ScoreCardMapper mapper;
    private final ScoreCardRepository repository;
    private final ScoreCardResourceValidator validator;

    private final CourseService courseService;
    private final PlayerService playerService;

    @Override
    public ScoreCardResource getScoreCard(Long id) throws NotFoundException {
        return repository.findById(id)
                .map(mapper::toResource)
                .orElseThrow(() -> new NotFoundException(new ScoreCard(), id));
    }

    @Override
    public ScoreCardResource addScoreCard(final NewScoreCardResource resource) throws ScoreCardException {
        NewScoreCardResource validated = validator.validateNewResource(resource);

        Course course = courseService.getCourse(validated.getCourseId().longValue());
        List<Player> players = playerService.getPlayers(validated.getPlayersIds());
        ScoreCard scoreCard = mapper.toScoreCard(course, players);

        ScoreCard saved = repository.save(scoreCard);
        return mapper.toResource(saved);
    }

    @Override
    public void deleteScoreCard(Long id) {
        repository.deleteById(id);
    }
}
