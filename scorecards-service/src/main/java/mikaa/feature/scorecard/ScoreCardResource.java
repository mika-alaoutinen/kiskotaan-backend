package mikaa.feature.scorecard;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import mikaa.api.ScoreCardsApi;
import mikaa.domain.Course;
import mikaa.domain.Hole;
import mikaa.domain.NewScoreCard;
import mikaa.domain.Player;
import mikaa.domain.ScoreCard;
import mikaa.logic.PlayerScore;
import mikaa.logic.ScoreCardInput;
import mikaa.logic.ScoreEntry;
import mikaa.logic.ScoreLogic;
import mikaa.model.CourseDTO;
import mikaa.model.NewScoreCardDTO;
import mikaa.model.PlayerDTO;
import mikaa.model.ResultDTO;
import mikaa.model.ScoreCardDTO;
import mikaa.model.ScoreCardSummaryDTO;
import mikaa.model.ScoreDTO;

@RequiredArgsConstructor
class ScoreCardResource implements ScoreCardsApi {

  private final ScoreCardService service;

  @Override
  @Transactional
  public ScoreCardDTO addScoreCard(@Valid @NotNull NewScoreCardDTO newScoreCardDTO) {
    var newScoreCard = new NewScoreCard(
        newScoreCardDTO.getCourseId().longValue(),
        newScoreCardDTO.getPlayerIds().stream().map(BigDecimal::longValue).toList());

    return toDto(service.add(newScoreCard));
  }

  @Override
  @Transactional
  public void deleteScoreCard(Integer id) {
    service.delete(id);
  }

  @Override
  public ScoreCardDTO getScoreCard(Integer id) {
    return toDto(service.findByIdOrThrow(id));
  }

  @Override
  @Transactional
  public List<ScoreCardSummaryDTO> getScoreCards() {
    return service.findAll()
        .stream()
        .map(ScoreCardResource::toSummary)
        .toList();
  }

  private static ScoreCardDTO toDto(ScoreCard scoreCard) {
    var input = ScoreCardInput.from(scoreCard);

    return new ScoreCardDTO()
        .id(BigDecimal.valueOf(scoreCard.id()))
        .course(mapCourse(scoreCard.course()))
        .players(mapPlayers(scoreCard.players()))
        .results(mapResults(ScoreLogic.results(input)))
        .scores(mapScores(ScoreLogic.scoresByPlayer(input)));
  }

  private static ScoreCardSummaryDTO toSummary(ScoreCard scoreCard) {
    var results = ScoreLogic.results(ScoreCardInput.from(scoreCard));

    return new ScoreCardSummaryDTO()
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

  private static Map<String, List<ScoreDTO>> mapScores(Map<Long, List<ScoreEntry>> scoresByPlayer) {
    return scoresByPlayer.entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> entry.getValue().stream().map(ScoreCardResource::mapScore).toList()));
  }

  private static ScoreDTO mapScore(ScoreEntry entry) {
    return new ScoreDTO()
        .id(BigDecimal.valueOf(entry.id()))
        .playerId((int) entry.playerId())
        .hole(entry.hole())
        .score(entry.score());
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
        .holesPlayed(ps.holesPlayed())
        .result(ps.result())
        .total(ps.total());
  }

}
