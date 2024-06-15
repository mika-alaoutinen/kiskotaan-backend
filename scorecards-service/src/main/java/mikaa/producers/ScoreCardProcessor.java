package mikaa.producers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import mikaa.config.IncomingChannels;
import mikaa.kiskotaan.scorecard.ScoreCardEvent;
import mikaa.kiskotaan.scorecard.ScoreCardGroupedScoresEvent;
import mikaa.kiskotaan.scorecard.ScoreCardGroupedScoresPayload;
import mikaa.kiskotaan.scorecard.ScoreEntry;

/**
 * Listens to the scorecard state topic and processes events sent to that topic
 * into two downstream topics.
 */
@ApplicationScoped
class ScoreCardProcessor {

  static final String SCORECARD_BY_PLAYER_STATE = "scorecard-by-player-state";
  static final String SCORECARD_BY_HOLE_STATE = "scorecard-by-hole-state";

  @Incoming(IncomingChannels.SCORECARD_STATE)
  @Outgoing(SCORECARD_BY_HOLE_STATE)
  Uni<Record<Long, ScoreCardGroupedScoresEvent>> sendScoresByHoleEvent(ScoreCardEvent event) {
    var scores = event.getPayload()
        .getScores()
        .stream()
        .collect(Collectors.groupingBy(score -> score.getHole() + ""));

    return sendEvent(event, scores);
  }

  @Incoming(IncomingChannels.SCORECARD_STATE)
  @Outgoing(SCORECARD_BY_PLAYER_STATE)
  Uni<Record<Long, ScoreCardGroupedScoresEvent>> sendScoresByPlayerEvent(ScoreCardEvent event) {
    var scores = event.getPayload()
        .getScores()
        .stream()
        .collect(Collectors.groupingBy(score -> score.getPlayerId() + ""));

    return sendEvent(event, scores);
  }

  private static Uni<Record<Long, ScoreCardGroupedScoresEvent>> sendEvent(
      ScoreCardEvent event,
      Map<String, List<ScoreEntry>> groupedScores) {
    var scoreCard = event.getPayload();

    var payload = new ScoreCardGroupedScoresPayload(
        scoreCard.getId(),
        scoreCard.getCourseId(),
        scoreCard.getPlayerIds(),
        scoreCard.getResults(),
        groupedScores);

    var scoreEvent = new ScoreCardGroupedScoresEvent(event.getAction(), payload);
    var record = Record.of(event.getPayload().getId(), scoreEvent);

    return Uni.createFrom().item(record);
  }

}
