package com.mika.kiskotaan.validators;

import com.mika.kiskotaan.errors.badrequest.ScoreCardException;
import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.services.CourseService;
import com.mika.kiskotaan.services.PlayerService;
import kiskotaan.openapi.model.NewScoreCardResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ScoreCardResourceValidator {
    private final CourseService courseService;
    private final PlayerService playerService;

    public NewScoreCardResource validateNewResource(NewScoreCardResource resource) throws ScoreCardException {
        validateCourseExists(resource.getCourseId());
        validatePlayersExist(resource.getPlayersIds());
        return resource;
    }

    private void validateCourseExists(BigDecimal courseId) throws ScoreCardException {
        if (!courseService.existsById(courseId.longValue())) {
            throw new ScoreCardException(new Course());
        }
    }

    private void validatePlayersExist(Set<BigDecimal> playerIds) throws ScoreCardException {
        if (!playerService.existsByIds(playerIds)) {
            throw new ScoreCardException(new Player());
        }
    }
}
