package mikaa.events;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class ScoreCardProducer {

  @Inject
  @Channel("scorecards-out")
  Emitter<String> emitter;

  public void send(String message) {
    var acked = emitter.send(message);
    acked.toCompletableFuture().join();
  }

}
