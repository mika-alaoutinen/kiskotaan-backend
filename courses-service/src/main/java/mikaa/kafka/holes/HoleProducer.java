package mikaa.kafka.holes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class HoleProducer {

  @Inject
  @Channel("holes-out")
  Emitter<HoleEvent> emitter;

  public void send(HoleEventType type, HolePayload payload) {
    var acked = emitter.send(new HoleEvent(type, payload));
    acked.toCompletableFuture().join();
  }

}
