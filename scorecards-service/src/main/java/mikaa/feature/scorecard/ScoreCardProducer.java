package mikaa.feature.scorecard;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

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
