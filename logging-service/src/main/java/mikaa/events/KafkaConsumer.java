package mikaa.events;

import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
class KafkaConsumer {

  private static final Logger log = LoggerFactory.getLogger(KafkaConsumer.class);

  @Incoming("courses-in")
  void consumeCourses(String event) {
    log.info(event);
  }

  @Incoming("holes-in")
  void consumeHoles(String event) {
    log.info(event);
  }

  @Incoming("players-in")
  void consumePlayers(String event) {
    log.info(event);
  }

  @Incoming("scorecards-in")
  void consumeScorecards(String event) {
    log.info(event);
  }

  @Incoming("scores-in")
  void consumeScores(String event) {
    log.info(event);
  }

}