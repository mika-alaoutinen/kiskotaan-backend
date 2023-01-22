package mikaa.kafka.holes;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import mikaa.dto.HoleDTO;

@ApplicationScoped
public class HoleProducer {

  @Inject
  @Channel("holes-out")
  Emitter<HoleEvent> emitter;

  public void send(HoleEventType type, HoleDTO hole) {
    var acked = emitter.send(new HoleEvent(type, hole));
    acked.toCompletableFuture().join();
  }

}
