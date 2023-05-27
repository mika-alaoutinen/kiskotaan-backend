package mikaa.queries;

import java.util.List;

import mikaa.queries.dto.CourseDTO;
import mikaa.queries.dto.PlayerDTO;
import mikaa.queries.dto.ScoreCardDTO;
import mikaa.queries.dto.ScoreCardPlayerDTO;

public interface TestData {

  public CourseDTO COURSE = new CourseDTO(1, "Frisbeegolf Laajis", 0, List.of());

  public PlayerDTO PLAYER = new PlayerDTO(2, "Pekka", "Kana");

  public static final ScoreCardPlayerDTO PLAYER_SC = new ScoreCardPlayerDTO(2, "Pekka", "Kana", List.of());

  public static final ScoreCardDTO SCORE_CARD = new ScoreCardDTO(
      3, COURSE, List.of(PLAYER_SC));

}
