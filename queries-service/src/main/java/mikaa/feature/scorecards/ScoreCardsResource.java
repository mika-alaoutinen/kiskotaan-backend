package mikaa.feature.scorecards;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@Path("scorecards")
@RequiredArgsConstructor
public class ScoreCardsResource {

  @GET
  @Path("/{id}")
  public Uni<String> getScoreCard(int id) {
    return Uni.createFrom().item("get one");
  }

  @GET
  public Multi<String> getScoreCards() {
    return Multi.createFrom().item("get many");

  }

}
