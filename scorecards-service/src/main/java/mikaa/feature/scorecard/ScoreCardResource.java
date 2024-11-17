package mikaa.feature.scorecard;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import lombok.RequiredArgsConstructor;
import mikaa.api.ScoreCardsApi;
import mikaa.domain.Course;
import mikaa.domain.Hole;
import mikaa.domain.NewScore;
import mikaa.domain.NewScoreCard;
import mikaa.domain.Player;
import mikaa.domain.ScoreCard;
import mikaa.logic.ScoreCardInput;
import mikaa.logic.ScoreEntry;
import mikaa.logic.ScoreLogic;
import mikaa.model.CourseDTO;
import mikaa.model.NewScoreCardDTO;
import mikaa.model.NewScoreDTO;
import mikaa.model.PlayerDTO;
import mikaa.model.ScoreCardDTO;
import mikaa.model.ScoreDTO;

@RequiredArgsConstructor
class ScoreCardResource implements ScoreCardsApi {

  private final ScoreService scoreService;
  private final ScoreCardService service;

  @Override
  @Transactional
  public ScoreDTO addScore(Integer id, @Valid @NotNull NewScoreDTO newScoreDTO) {
    var newScore = new NewScore(newScoreDTO.getPlayerId().longValue(), newScoreDTO.getHole(), newScoreDTO.getScore());
    var score = scoreService.addScore(id, newScore);
    return ScoreMapper.dto(score);
  }

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

  private static ScoreCardDTO toDto(ScoreCard scoreCard) {
    var input = ScoreCardInput.from(scoreCard);
    var scores = ScoreLogic.scoresByPlayer(input)
        .values()
        .stream()
        .flatMap(Collection::stream)
        .toList();

    return new ScoreCardDTO()
        .id(BigDecimal.valueOf(scoreCard.id()))
        .course(mapCourse(scoreCard.course()))
        .players(mapPlayers(scoreCard.players()))
        .scores(mapScores(scores));
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

  private static List<ScoreDTO> mapScores(List<ScoreEntry> scores) {
    return scores.stream()
        .map(score -> new ScoreDTO()
            .id(BigDecimal.valueOf(score.id()))
            .playerId((int) score.playerId())
            .hole(score.hole())
            .score(score.score()))
        .toList();
  }

}
