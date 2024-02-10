package mikaa.producers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.scorecard.RoundResult;
import mikaa.kiskotaan.scorecard.ScoreCardByPlayerPayload;
import mikaa.kiskotaan.scorecard.ScoreCardPayload;
import mikaa.kiskotaan.scorecard.ScoreEntry;
import mikaa.logic.Mapper;
import mikaa.logic.ScoreLogic;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardByPlayerProducer {

  private final ModelMapper mapper;

  ScoreCardByPlayerPayload mapPayload(ScoreCardPayload scoreCard) {
    var input = Mapper.toInput(scoreCard);
    var scoresByHole = ScoreLogic.scoresByHole(input);

    var results = scoresByHole.getResults()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> mapper.map(entry.getValue(), RoundResult.class)));

    var scores = ScoreLogic.scoresByPlayer(input)
        .getScores()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> mapMany(entry.getValue(), ScoreEntry.class)));

    return new ScoreCardByPlayerPayload(
        scoreCard.getId(),
        scoreCard.getCourseId(),
        scoreCard.getPlayerIds(),
        results,
        scores);
  }

  private <T, R> List<R> mapMany(Collection<T> entities, Class<R> type) {
    return entities.stream().map(e -> mapper.map(e, type)).toList();
  }

}
