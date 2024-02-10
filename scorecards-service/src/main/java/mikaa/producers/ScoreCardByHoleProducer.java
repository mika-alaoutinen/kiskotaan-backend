package mikaa.producers;

import java.util.stream.Collectors;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.smallrye.reactive.messaging.kafka.Record;
import jakarta.enterprise.context.ApplicationScoped;
import mikaa.config.OutgoingChannels;
import mikaa.kiskotaan.scorecard.ScoreCardEvent;
import mikaa.kiskotaan.scorecard.ScoreCardGroupedScoresEvent;
import mikaa.kiskotaan.scorecard.ScoreCardGroupedScoresPayload;

@ApplicationScoped
class ScoreCardByHoleProducer {

  @Incoming(ScoreCardProducer.INTERNAL_SCORECARD_CHANNEL)
  @Outgoing(OutgoingChannels.SCORECARD_BY_HOLE_STATE)
  Record<Long, ScoreCardGroupedScoresEvent> process(ScoreCardEvent event) {
    var scoreCard = event.getPayload();

    var scores = scoreCard.getScores().stream().collect(
        Collectors.groupingBy(score -> score.getHole() + ""));

    var payload = new ScoreCardGroupedScoresPayload(
        scoreCard.getId(),
        scoreCard.getCourseId(),
        scoreCard.getPlayerIds(),
        scoreCard.getResults(),
        scores);

    var scoreEvent = new ScoreCardGroupedScoresEvent(event.getAction(), payload);

    return Record.of(event.getPayload().getId(), scoreEvent);
  }

}
