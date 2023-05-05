package mikaa.feature.scorecards;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
class ScoreCardsResource {

  public Uni<String> hello() {
    return Uni.createFrom().item("Reactive score cards");
  }

  public Uni<Void> getScoreCard(int id) {
    throw new UnsupportedOperationException("Unimplemented method 'getScoreCard'");
  }

  public Multi<Void> getScoreCards() {
    throw new UnsupportedOperationException("Unimplemented method 'getScoreCards'");
  }

}
