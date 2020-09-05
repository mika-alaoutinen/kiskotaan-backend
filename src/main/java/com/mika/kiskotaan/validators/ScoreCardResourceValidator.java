package com.mika.kiskotaan.validators;

import com.mika.kiskotaan.errors.badrequest.ScoreCardException;
import com.mika.kiskotaan.services.CourseService;
import com.mika.kiskotaan.services.PlayerService;
import kiskotaan.openapi.model.CourseResource;
import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.PlayerResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScoreCardResourceValidator {
    private final CourseService courseService;
    private final PlayerService playerService;

    public NewScoreCardResource validateNewResource(NewScoreCardResource resource) throws ScoreCardException {
        validateCourseExists(resource.getCourse());
        validatePlayersExist(resource.getPlayers());
        return resource;
    }

    private void validateCourseExists(CourseResource courseResource) throws ScoreCardException {
        if (!courseService.existsById(courseResource.getId().longValue())) {
            throw new ScoreCardException(courseResource);
        }
    }

    private void validatePlayersExist(List<PlayerResource> playerResources) throws ScoreCardException {
        Set<Long> playerIds = playerResources.stream()
                .mapToLong(player -> player.getId().longValue())
                .boxed()
                .collect(Collectors.toSet());

        if (!playerService.existsByIds(playerIds)) {
            throw new ScoreCardException(new PlayerResource());
        }
    }
}
