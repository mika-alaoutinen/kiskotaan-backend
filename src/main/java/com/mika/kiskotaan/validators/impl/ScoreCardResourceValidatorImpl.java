package com.mika.kiskotaan.validators.impl;

import com.mika.kiskotaan.dao.CourseDao;
import com.mika.kiskotaan.dao.PlayerDao;
import com.mika.kiskotaan.errors.badrequest.ScoreCardException;
import com.mika.kiskotaan.mappers.MapperUtils;
import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.validators.ScoreCardResourceValidator;
import kiskotaan.openapi.model.NewScoreCardResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ScoreCardResourceValidatorImpl implements ScoreCardResourceValidator {
    private final CourseDao courseDao;
    private final PlayerDao playerDao;

    public NewScoreCardResource validateNewResource(NewScoreCardResource resource) throws ScoreCardException {
        validateCourseExists(resource.getCourseId());
        validatePlayersExist(resource.getPlayersIds());
        return resource;
    }

    private void validateCourseExists(BigDecimal courseId) throws ScoreCardException {
        if (!courseDao.existsById(courseId.longValue())) {
            throw new ScoreCardException(new Course());
        }
    }

    private void validatePlayersExist(Set<BigDecimal> playerIds) throws ScoreCardException {
        Collection<Long> ids = MapperUtils.mapIds(playerIds);

        if (!playerDao.existsByIds(ids)) {
            throw new ScoreCardException(new Player());
        }
    }
}
