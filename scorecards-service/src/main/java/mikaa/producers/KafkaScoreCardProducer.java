package mikaa.producers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import mikaa.config.OutgoingChannels;
import mikaa.kiskotaan.scorecard.ScoreCardEvent;
import mikaa.kiskotaan.scorecard.ScoreCardGroupedScoresEvent;
import mikaa.kiskotaan.scorecard.ScoreCardGroupedScoresPayload;
import mikaa.kiskotaan.scorecard.ScoreEntry;

@ApplicationScoped
class KafkaScoreCardProducer {

  @Incoming(ScoreCardProducer.INTERNAL_SCORECARD_CHANNEL)
  @Outgoing(OutgoingChannels.SCORECARD_STATE)
  Record<Long, ScoreCardEvent> processStateEvent(ScoreCardEvent event) {
    return Record.of(event.getPayload().getId(), event);
  }

  @Incoming(ScoreCardProducer.INTERNAL_SCORECARD_CHANNEL)
  @Outgoing(OutgoingChannels.SCORECARD_BY_HOLE_STATE)
  Record<Long, ScoreCardGroupedScoresEvent> processScoresByHoleEvent(ScoreCardEvent event) {
    var scores = event.getPayload()
        .getScores()
        .stream()
        .collect(
            Collectors.groupingBy(score -> score.getHole() + ""));

    return sendEvent(event, scores);
  }

  @Incoming(ScoreCardProducer.INTERNAL_SCORECARD_CHANNEL)
  @Outgoing(OutgoingChannels.SCORECARD_BY_PLAYER_STATE)
  Record<Long, ScoreCardGroupedScoresEvent> processScoresByPlayerEvent(ScoreCardEvent event) {
    var scores = event.getPayload()
        .getScores()
        .stream()
        .collect(Collectors.groupingBy(score -> score.getPlayerId() + ""));

    return sendEvent(event, scores);
  }

  private static Record<Long, ScoreCardGroupedScoresEvent> sendEvent(
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

    return Record.of(event.getPayload().getId(), scoreEvent);
  }

}