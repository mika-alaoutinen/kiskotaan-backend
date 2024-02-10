package mikaa.producers;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.scorecard.RoundResult;
import mikaa.kiskotaan.scorecard.ScoreCardByHolePayload;
import mikaa.kiskotaan.scorecard.ScoreCardPayload;
import mikaa.logic.Mapper;
import mikaa.logic.ScoreLogic;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardByHoleProducer {

  private final ModelMapper mapper;

  ScoreCardByHolePayload mapPayload(ScoreCardPayload scoreCard) {
    var results = ScoreLogic.scoresByHole(Mapper.toInput(scoreCard))
        .getResults()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> mapper.map(entry.getValue(), RoundResult.class)));

    return new ScoreCardByHolePayload(
        scoreCard.getId(),
        scoreCard.getCourseId(),
        scoreCard.getPlayerIds(),
        results);
  }

}
