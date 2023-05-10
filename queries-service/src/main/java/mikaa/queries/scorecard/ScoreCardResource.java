package mikaa.queries.scorecard;

import java.util.List;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import mikaa.dto.ScoreCardDTO;
import mikaa.feature.MockData;

@ApplicationScoped
@Path("scorecards")
@RequiredArgsConstructor
public class ScoreCardResource {

  @GET
  @Path("/{id}")
  public Uni<ScoreCardDTO> getScoreCard(int id) {
    return Uni.createFrom().item(MockData.SCORE_CARD);
  }

  @GET
  public Uni<List<ScoreCardDTO>> getScoreCards() {
    return Uni.createFrom().item(List.of(MockData.SCORE_CARD));
  }

}
