package mikaa.producers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.scorecards.RoundResult;
import mikaa.kiskotaan.scorecards.ScoreCardByPlayerPayload;
import mikaa.kiskotaan.scorecards.ScoreEntry;
import mikaa.logic.ScoreLogic;
import mikaa.feature.scorecard.ScoreCardEntity;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardByPlayerProducer {

  private final ModelMapper mapper;

  // ScoreCardByPlayerPayload mapPayload(ScoreCardPayload scoreCard) { ... }
  ScoreCardByPlayerPayload mapPayload(ScoreCardEntity scoreCard) {
    var scoresByHole = ScoreLogic.calculateScoresByHole(scoreCard);

    var results = scoresByHole.getResults()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> mapper.map(entry.getValue(), RoundResult.class)));

    var scores = ScoreLogic.calculateScoresByPlayer(scoreCard)
        .getScores()
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> mapMany(entry.getValue(), ScoreEntry.class)));

    return new ScoreCardByPlayerPayload(
        scoreCard.getId(),
        scoreCard.getCourse().getExternalId(),
        scoreCard.getPlayerIds(),
        results,
        scores);
  }

  private <T, R> List<R> mapMany(Collection<T> entities, Class<R> type) {
    return entities.stream().map(e -> mapper.map(e, type)).toList();
  }

}
