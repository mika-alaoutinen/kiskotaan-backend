package mikaa.domain;

import java.util.Collection;

public record NewScoreCard(long courseId, Collection<Long> playerIds) {
}
