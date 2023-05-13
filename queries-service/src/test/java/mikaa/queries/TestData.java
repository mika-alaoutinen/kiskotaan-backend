package mikaa.queries;

import java.util.List;
import java.util.Map;

import mikaa.queries.dto.CourseDTO;
import mikaa.queries.dto.PlayerDTO;
import mikaa.queries.dto.ScoreCardDTO;

public interface TestData {

  public CourseDTO COURSE = new CourseDTO(1, "Frisbeegolf Laajis", 0, List.of());

  public PlayerDTO PLAYER = new PlayerDTO(2, "Pekka", "Kana");

  public ScoreCardDTO SCORE_CARD = new ScoreCardDTO(
      3,
      COURSE,
      List.of(PLAYER),
      Map.of(PLAYER.id(), List.of()));

}
