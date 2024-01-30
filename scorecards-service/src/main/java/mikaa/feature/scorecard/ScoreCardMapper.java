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
import mikaa.logic.ScoreEntry;
import mikaa.logic.ScoreLogic;
import mikaa.model.CourseDTO;
import mikaa.model.PlayerDTO;
import mikaa.model.ResultDTO;
import mikaa.model.ScoreCardDTO;
import mikaa.model.ScoreCardSummaryDTO;
import mikaa.model.ScoreDTO;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardMapper {

  private final ModelMapper mapper;

  ScoreCardDTO toDto(ScoreCardEntity scoreCard) {
    var scoresByPlayer = ScoreLogic.calculateScoresByPlayer(scoreCard);

    return new ScoreCardDTO()
        .id(BigDecimal.valueOf(scoreCard.getId()))
        .course(toCourse(scoreCard.getCourse()))
        .players(mapMany(scoreCard.getPlayers(), PlayerDTO.class))
        .results(mapResults(scoresByPlayer.getResults()))
        .scores(scoresByPlayer(scoresByPlayer.getScores()));
  }

  private CourseDTO toCourse(CourseEntity course) {
    var coursePar = course.getHoles().stream().mapToInt(HoleEntity::getPar).sum();

    return new CourseDTO()
        .id(BigDecimal.valueOf(course.getExternalId()))
        .holes(course.getHoles().size())
        .name(course.getName())
        .par(coursePar);
  }

  private Map<String, List<ScoreDTO>> scoresByPlayer(Map<Long, List<ScoreEntry>> scores) {
    return scores.entrySet().stream().collect(
        Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> mapMany(entry.getValue(), ScoreDTO.class)));
  }

  ScoreCardSummaryDTO toSummary(ScoreCardEntity scoreCard) {
    var scoresByPlayer = ScoreLogic.calculateScoresByPlayer(scoreCard);

    return new ScoreCardSummaryDTO()
        .id(BigDecimal.valueOf(scoreCard.getId()))
        .course(toCourse(scoreCard.getCourse()))
        .players(mapMany(scoreCard.getPlayers(), PlayerDTO.class))
        .results(mapResults(scoresByPlayer.getResults()));
  }

  private Map<String, ResultDTO> mapResults(Map<Long, PlayerScore> results) {
    return results.entrySet()
        .stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toString(),
            entry -> mapper.map(entry.getValue(), ResultDTO.class)));
  }

  private <T, R> List<R> mapMany(Collection<T> entities, Class<R> type) {
    return entities.stream().map(e -> mapper.map(e, type)).toList();
  }

}
