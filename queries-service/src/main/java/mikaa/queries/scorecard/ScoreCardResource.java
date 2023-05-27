package mikaa.queries.scorecard;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import mikaa.queries.dto.ScoreCardDTO;

@Path("scorecards")
@RequiredArgsConstructor
public class ScoreCardResource {

  private final ScoreCardReader scoreCards;

  @GET
  @Path("/{id}")
  public Uni<ScoreCardDTO> getScoreCard(int id) {
    return scoreCards.findOne(id).map(ScoreCardMapper::toDto);
  }

  @GET
  public Multi<ScoreCardDTO> getScoreCards() {
    return scoreCards.findAll().map(ScoreCardMapper::toDto);
  }

}
