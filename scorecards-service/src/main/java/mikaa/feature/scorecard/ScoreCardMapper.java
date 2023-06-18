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
import mikaa.logic.PlayerScore;
import mikaa.logic.ScoreLogic;
import mikaa.model.CourseDTO;
import mikaa.model.PlayerDTO;
import mikaa.model.PlayerScoreDTO;
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
        .players(mapMany(scoreCard.getPlayers(), PlayerDTO.class))
        .scores(scoresByPlayer(scoreCard));
  }

  private CourseDTO toCourse(CourseEntity course) {
    var coursePar = course.getHoles().stream().mapToInt(HoleEntity::getPar).sum();

    return new CourseDTO()
        .id(BigDecimal.valueOf(course.getExternalId()))
        .holes(course.getHoles().size())
        .name(course.getName())
        .par(coursePar);
  }

  private Map<String, PlayerScoreDTO> scoresByPlayer(ScoreCardEntity scoreCard) {
    return ScoreLogic.calculatePlayerScores(scoreCard)
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> mapper.map(entry.getValue(), PlayerScoreDTO.class)));
  }

  ScoreCardSummaryDTO toSummary(ScoreCardEntity scoreCard) {
    return new ScoreCardSummaryDTO()
        .id(BigDecimal.valueOf(scoreCard.getId()))
        .course(toCourse(scoreCard.getCourse()))
        .players(mapMany(scoreCard.getPlayers(), PlayerDTO.class))
        .scores(toScoreSummaries(scoreCard));
  }

  private static Map<String, PlayerScoreSummaryDTO> toScoreSummaries(ScoreCardEntity scoreCard) {
    return ScoreLogic.calculatePlayerScores(scoreCard)
        .entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> toPlayerScoreSummary(entry.getValue(), scoreCard.getCourse())));
  }

  private static PlayerScoreSummaryDTO toPlayerScoreSummary(PlayerScore playerScore, CourseEntity course) {
    return new PlayerScoreSummaryDTO()
        .holesPlayed(playerScore.getEntries().size())
        .result(playerScore.getResult())
        .total(playerScore.getTotal());
  }

  private <T, R> List<R> mapMany(Collection<T> entities, Class<R> type) {
    return entities.stream().map(e -> mapper.map(e, type)).toList();
  }

}
