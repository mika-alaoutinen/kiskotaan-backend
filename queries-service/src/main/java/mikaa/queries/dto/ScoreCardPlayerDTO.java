package mikaa.queries.dto;

import java.util.List;

public record ScoreCardPlayerDTO(
    long id,
    String firstName,
    String lastName,
    List<ScoreDTO> scores) {

}
