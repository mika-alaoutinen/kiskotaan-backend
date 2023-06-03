package mikaa.feature.scorecard;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.feature.course.CourseEntity;
import mikaa.feature.course.HoleEntity;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.score.ScoreEntity;
import mikaa.model.CourseDTO;
import mikaa.model.PlayerDTO;
import mikaa.model.PlayerScoreDTO;
import mikaa.model.PlayerScoreEntryDTO;
import mikaa.model.PlayerScoreSummaryDTO;
import mikaa.model.ScoreCardDTO;
import mikaa.model.ScoreCardSummaryDTO;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardMapper {

  private final ModelMapper mapper;

  ScoreCardDTO toDto(ScoreCardEntity scoreCard) {
    return new ScoreCardDTO()
        .id(BigDecimal.valueOf(scoreCard.getId()))
        .course(toCourse(scoreCard.getCourse()))
        .players(toPlayers(scoreCard.getPlayers()))
        .scores(toPlayerScores(scoreCard));
  }

  private CourseDTO toCourse(CourseEntity course) {
    var coursePar = course.getHoles().stream().mapToInt(HoleEntity::getPar).sum();

    return new CourseDTO()
        .id(BigDecimal.valueOf(course.getExternalId()))
        .holes(course.getHoles().size())
        .name(course.getName())
        .par(coursePar);
  }

  private List<PlayerDTO> toPlayers(Collection<PlayerEntity> players) {
    return players.stream().map(p -> mapper.map(p, PlayerDTO.class)).toList();
  }

  private Map<String, PlayerScoreDTO> toPlayerScores(ScoreCardEntity scoreCard) {
    var course = scoreCard.getCourse();
    var playerScores = scoresByPlayers(scoreCard.getScores());

    return playerScores.entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> toPlayerScore(entry.getValue(), course)));
  }

  private PlayerScoreDTO toPlayerScore(Collection<ScoreEntity> playerScores, CourseEntity course) {
    var entries = playerScores.stream()
        .map(s -> mapper.map(s, PlayerScoreEntryDTO.class))
        .toList();

    return new PlayerScoreDTO()
        .entries(entries)
        .result(ScoreCalculator.result(playerScores, course))
        .total(ScoreCalculator.total(playerScores));
  }

  ScoreCardSummaryDTO toSummary(ScoreCardEntity scoreCard) {
    return new ScoreCardSummaryDTO()
        .id(BigDecimal.valueOf(scoreCard.getId()))
        .course(toCourse(scoreCard.getCourse()))
        .players(toPlayers(scoreCard.getPlayers()))
        .scores(toScoreSummaries(scoreCard));
  }

  private static Map<String, PlayerScoreSummaryDTO> toScoreSummaries(ScoreCardEntity scoreCard) {
    var course = scoreCard.getCourse();
    var playerScores = scoresByPlayers(scoreCard.getScores());

    return playerScores.entrySet()
        .stream()
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            entry -> toPlayerScoreSummary(entry.getValue(), course)));
  }

  private static PlayerScoreSummaryDTO toPlayerScoreSummary(Collection<ScoreEntity> playerScores, CourseEntity course) {
    return new PlayerScoreSummaryDTO()
        .holesPlayed(playerScores.size())
        .result(ScoreCalculator.result(playerScores, course))
        .total(ScoreCalculator.total(playerScores));
  }

  private static Map<String, List<ScoreEntity>> scoresByPlayers(Collection<ScoreEntity> scores) {
    return scores.stream().collect(
        Collectors.groupingBy(score -> score.getPlayer().getExternalId() + ""));
  }

}
