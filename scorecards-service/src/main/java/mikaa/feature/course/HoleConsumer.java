package mikaa.feature.course;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mikaa.events.hole.HoleEvent;

@ApplicationScoped
class HoleConsumer {

  private static final Logger log = LoggerFactory.getLogger(HoleConsumer.class);

  @Incoming("holes-in")
  @Transactional
  void consume(HoleEvent event) {
    log.info("type " + event.type());
    log.info("payload " + event.payload());
  }

}
