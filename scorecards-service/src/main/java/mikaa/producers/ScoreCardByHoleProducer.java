package mikaa.producers;

import java.util.stream.Collectors;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.modelmapper.ModelMapper;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.config.OutgoingChannels;
import mikaa.kiskotaan.scorecard.ScoreCardEvent;
import mikaa.kiskotaan.scorecard.ScoreCardGroupedScoresEvent;
import mikaa.kiskotaan.scorecard.ScoreCardGroupedScoresPayload;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardByHoleProducer {

  private final ModelMapper mapper;

  @Incoming(ScoreCardProducer.INTERNAL_SCORECARD_CHANNEL)
  @Outgoing(OutgoingChannels.SCORECARD_BY_HOLE_STATE)
  ScoreCardGroupedScoresEvent process(ScoreCardEvent event) {
    var scoreCard = event.getPayload();

    var scores = scoreCard.getScores().stream().collect(
        Collectors.groupingBy(score -> score.getHole() + ""));

    var payload = new ScoreCardGroupedScoresPayload(
        scoreCard.getId(),
        scoreCard.getCourseId(),
        scoreCard.getPlayerIds(),
        scoreCard.getResults(),
        scores);

    return new ScoreCardGroupedScoresEvent(event.getAction(), payload);
  }

}
