package mikaa.feature.scorecard;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
class ScoreCardProducer {

  @Inject
  @Channel("scorecards-out")
  Emitter<String> emitter;

  void send(String message) {
    var acked = emitter.send(message);
    acked.toCompletableFuture().join();
  }

}
