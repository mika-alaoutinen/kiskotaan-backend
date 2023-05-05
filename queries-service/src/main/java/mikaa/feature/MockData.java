package mikaa.feature;

import java.util.List;
import java.util.Map;

import mikaa.dto.CourseDTO;
import mikaa.dto.PlayerDTO;
import mikaa.dto.ScoreCardDTO;

public interface MockData {

  public static final CourseDTO COURSE = new CourseDTO(1, "Frisbeegolf Laajis", 0, List.of());

  public static final PlayerDTO PLAYER = new PlayerDTO(2, "Pekka", "Kana");

  public static final ScoreCardDTO SCORE_CARD = new ScoreCardDTO(
      3,
      COURSE,
      List.of(PLAYER),
      Map.of(PLAYER.id(), List.of()));

}
