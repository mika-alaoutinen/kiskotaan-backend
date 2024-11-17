package mikaa.feature.scorecard;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mikaa.api.RoundResultsApi;
import mikaa.domain.Course;
import mikaa.domain.Hole;
import mikaa.domain.Player;
import mikaa.domain.ScoreCard;
import mikaa.feature.results.PlayerScore;
import mikaa.feature.results.ScoreCardInput;
import mikaa.feature.results.ScoreLogic;
import mikaa.model.CourseDTO;
import mikaa.model.PlayerDTO;
import mikaa.model.ResultDTO;
import mikaa.model.RoundResultDTO;

@ApplicationScoped
@RequiredArgsConstructor
class RoundResultsResource implements RoundResultsApi {

  private final ScoreCardService service;

  @Override
  public RoundResultDTO getRoundResult(Integer id) {
    return fromScoreCard(service.findByIdOrThrow(id));
  }

  @Override
  @Transactional
  public List<RoundResultDTO> getRoundResults() {
    return service.findAll()
        .stream()
        .map(RoundResultsResource::fromScoreCard)
        .toList();
  }

  private static RoundResultDTO fromScoreCard(ScoreCard scoreCard) {
    var results = ScoreLogic.results(ScoreCardInput.from(scoreCard));

    return new RoundResultDTO()
        .id(BigDecimal.valueOf(scoreCard.id()))
        .course(mapCourse(scoreCard.course()))
        .players(mapPlayers(scoreCard.players()))
        .results(mapResults(results));
  }

  private static CourseDTO mapCourse(Course c) {
    return new CourseDTO()
        .id(BigDecimal.valueOf(c.id()))
        .holes(c.holes().size())
        .name(c.name())
        .par(c.holes().stream().mapToInt(Hole::par).sum());
  }

  private static List<PlayerDTO> mapPlayers(Collection<Player> players) {
    return players.stream()
        .map(p -> new PlayerDTO()
            .id(BigDecimal.valueOf(p.id()))
            .firstName(p.firstName())
            .lastName(p.lastName()))
        .toList();
  }

  private static List<ResultDTO> mapResults(Map<Long, PlayerScore> results) {
    return results.values()
        .stream()
        .map(playerScore -> new ResultDTO()
            .holesPlayed(playerScore.holesPlayed())
            .result(playerScore.result())
            .total(playerScore.total()))
        .toList();
  }

}
