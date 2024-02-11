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
import mikaa.config.OutgoingChannels;
import mikaa.kiskotaan.scorecard.ScoreCardEvent;
import mikaa.kiskotaan.scorecard.ScoreCardGroupedScoresEvent;
import mikaa.kiskotaan.scorecard.ScoreCardGroupedScoresPayload;
import mikaa.kiskotaan.scorecard.ScoreEntry;

@ApplicationScoped
class KafkaScoreCardProducer {

  @Incoming(IncomingChannels.SCORECARD_STATE)
  @Outgoing(OutgoingChannels.SCORECARD_BY_HOLE_STATE)
  Uni<Record<Long, ScoreCardGroupedScoresEvent>> processScoresByHoleEvent(ScoreCardEvent event) {
    var scores = event.getPayload()
        .getScores()
        .stream()
        .collect(Collectors.groupingBy(score -> score.getHole() + ""));

    return sendEvent(event, scores);
  }

  @Incoming(IncomingChannels.SCORECARD_STATE)
  @Outgoing(OutgoingChannels.SCORECARD_BY_PLAYER_STATE)
  Uni<Record<Long, ScoreCardGroupedScoresEvent>> processScoresByPlayerEvent(ScoreCardEvent event) {
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
