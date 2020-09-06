package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.dao.CourseDao;
import com.mika.kiskotaan.dao.PlayerDao;
import com.mika.kiskotaan.dao.ScoreCardDao;
import com.mika.kiskotaan.errors.badrequest.ScoreCardException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.MapperUtils;
import com.mika.kiskotaan.mappers.ScoreCardMapper;
import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.services.ScoreCardService;
import com.mika.kiskotaan.validators.ScoreCardResourceValidator;
import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.ScoreCardResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ScoreCardServiceImpl implements ScoreCardService {
    private final CourseDao courseDao;
    private final PlayerDao playerDao;
    private final ScoreCardDao dao;
    private final ScoreCardMapper mapper;
    private final ScoreCardResourceValidator validator;

    @Override
    public ScoreCardResource getScoreCard(Long id) throws NotFoundException {
        return dao.getScoreCard(id)
                .map(mapper::toResource)
                .orElseThrow(() -> new NotFoundException(new ScoreCard(), id));
    }

    @Override
    public ScoreCardResource addScoreCard(final NewScoreCardResource newScoreCard) throws ScoreCardException {
        return Stream.of(validator.validateNewResource(newScoreCard))
                .map(this::createScoreCard)
                .map(dao::addScoreCard)
                .map(mapper::toResource)
                .findAny()
                .orElseThrow(() -> new ScoreCardException(newScoreCard));
    }

    @Override
    public void deleteScoreCard(Long id) {
        dao.deleteScoreCard(id);
    }

    private ScoreCard createScoreCard(NewScoreCardResource resource) {
        Course course = courseDao.getCourse(resource.getCourseId().longValue())
                .orElseThrow(() -> new ScoreCardException(new Course()));

        var playerIds = MapperUtils.mapIds(resource.getPlayersIds());
        var players = playerDao.getPlayersByIds(playerIds);

        return new ScoreCard(course, players, new ArrayList<>());
    }
}