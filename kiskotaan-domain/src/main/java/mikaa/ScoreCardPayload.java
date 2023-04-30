package mikaa;

import java.util.List;

public record ScoreCardPayload(Long id, long courseId, List<Long> playerIds) {
}
