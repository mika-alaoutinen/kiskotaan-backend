package mikaa.producers;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.kiskotaan.scorecards.RoundResult;
import mikaa.kiskotaan.scorecards.ScoreCardByHolePayload;
import mikaa.logic.ScoreLogic;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardByHoleProducer {

  private final ModelMapper mapper;

  // ScoreCardByHolePayload mapPayload(ScoreCardPayload scoreCard) { ... }
  ScoreCardByHolePayload mapPayload(ScoreCardEntity scoreCard) {
    var results = ScoreLogic.calculateScoresByHole(scoreCard)
        .getResults()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> mapper.map(entry.getValue(), RoundResult.class)));

    return new ScoreCardByHolePayload(
        scoreCard.getId(),
        scoreCard.getCourse().getExternalId(),
        scoreCard.getPlayerIds(),
        results);
  }

}
