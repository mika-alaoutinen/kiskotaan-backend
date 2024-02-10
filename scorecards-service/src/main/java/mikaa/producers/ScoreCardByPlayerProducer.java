package mikaa.producers;

import java.util.stream.Collectors;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import jakarta.enterprise.context.ApplicationScoped;
import mikaa.config.OutgoingChannels;
import mikaa.kiskotaan.scorecard.ScoreCardByPlayerEvent;
import mikaa.kiskotaan.scorecard.ScoreCardByPlayerPayload;
import mikaa.kiskotaan.scorecard.ScoreCardEvent;

@ApplicationScoped
class ScoreCardByPlayerProducer {

  @Incoming(ScoreCardProducer.INTERNAL_SCORECARD_CHANNEL)
  @Outgoing(OutgoingChannels.SCORECARD_BY_PLAYER_STATE)
  ScoreCardByPlayerEvent groupScoresByPlayer(ScoreCardEvent event) {
    var scoreCard = event.getPayload();

    var scores = scoreCard.getScores().stream().collect(
        Collectors.groupingBy(score -> score.getPlayerId() + ""));

    var payload = new ScoreCardByPlayerPayload(
        scoreCard.getId(),
        scoreCard.getCourseId(),
        scoreCard.getPlayerIds(),
        scoreCard.getResults(),
        scores);

    return new ScoreCardByPlayerEvent(event.getAction(), payload);
  }

}
