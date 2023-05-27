package mikaa.queries.scorecard;

import java.util.List;

record PlayerDTO(
    long id,
    String firstName,
    String lastName,
    List<ScoreDTO> scores) {

}
