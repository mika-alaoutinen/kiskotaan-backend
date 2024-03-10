package mikaa.feature.scorecard;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mikaa.domain.Course;
import mikaa.domain.Hole;
import mikaa.domain.Player;
import mikaa.domain.ScoreCard;
import mikaa.logic.PlayerScore;
import mikaa.logic.ScoreCardInput;
import mikaa.logic.ScoreEntry;
import mikaa.logic.ScoreLogic;
import mikaa.model.CourseDTO;
import mikaa.model.PlayerDTO;
import mikaa.model.ResultDTO;
import mikaa.model.ScoreCardDTO;
import mikaa.model.ScoreCardSummaryDTO;
import mikaa.model.ScoreDTO;

interface ScoreCardMapper {

  static ScoreCardDTO toDto(ScoreCard scoreCard) {
    var scoresByPlayer = ScoreLogic.scoresByPlayer(ScoreCardInput.from(scoreCard));

    return new ScoreCardDTO()
        .id(BigDecimal.valueOf(scoreCard.id()))
        .course(mapCourse(scoreCard.course()))
        .players(mapPlayers(scoreCard.players()))
        .results(mapResults(scoresByPlayer.getResults()))
        .scores(mapScores(scoresByPlayer.getScores()));
  }

  static ScoreCardSummaryDTO toSummary(ScoreCard scoreCard) {
    var scoresByPlayer = ScoreLogic.scoresByPlayer(ScoreCardInput.from(scoreCard));

    return new ScoreCardSummaryDTO()
        .id(BigDecimal.valueOf(scoreCard.id()))
        .course(mapCourse(scoreCard.course()))
        .players(mapPlayers(scoreCard.players()))
        .results(mapResults(scoresByPlayer.getResults()));
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

  private static Map<String, List<ScoreDTO>> mapScores(Map<Long, List<ScoreEntry>> scoresByPlayer) {
    return scoresByPlayer.entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> entry.getValue().stream().map(ScoreCardMapper::mapScore).toList()));
  }

  private static ScoreDTO mapScore(ScoreEntry entry) {
    return new ScoreDTO()
        .id(BigDecimal.valueOf(entry.getId()))
        .playerId((int) entry.getPlayerId())
        .hole(entry.getHole())
        .score(entry.getScore());
  }

  private static Map<String, ResultDTO> mapResults(Map<Long, PlayerScore> results) {
    return results.entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> mapResult(entry.getValue())));
  }

  private static ResultDTO mapResult(PlayerScore ps) {
    return new ResultDTO()
        .holesPlayed(ps.getHolesPlayed())
        .result(ps.getResult())
        .total(ps.getTotal());
  }

}
