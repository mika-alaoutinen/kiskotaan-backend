package mikaa.domain;

import java.util.Collection;

public record ScoreCard(
        long id,
        Course course,
        Collection<Player> players,
        Collection<Score> scores) {
}
