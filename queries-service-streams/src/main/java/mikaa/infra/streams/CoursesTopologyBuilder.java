package mikaa.infra.streams;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.Stores;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.domain.CourseEvent;
import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.streams.KafkaStreamsConfig;
import mikaa.streams.TopologyDescription.CoursesTopology;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class CoursesTopologyBuilder implements CoursesTopology {

  private final KafkaStreamsConfig config;
  private final SerdeConfigurer serdes;

  @Override
  public Description<Long, CourseEvent, CoursePayload> description() {
    var keySerde = Serdes.Long();
    var input = new Topic<>(inputTopic(), keySerde, inputSerde());
    var output = new Topic<>(outputTopic(), keySerde, outputSerde());
    return new Description<>(input, output);
  }

  void build(StreamsBuilder builder) {
    var keySerde = Serdes.Long();

    builder
        .stream(inputTopic(), Consumed.with(keySerde, inputSerde()))
        .peek((id, event) -> logEvent(event))
        .mapValues(CoursesTopologyBuilder::processEvent)
        .toTable(Materialized.<Long, CoursePayload>as(Stores.persistentKeyValueStore(outputTopic()))
            .withKeySerde(keySerde)
            .withValueSerde(outputSerde()));
  }

  private static void logEvent(CourseEvent event) {
    log.info("Received course event: [ action {}, course ID {} ]", event.getAction(), event.getPayload().getId());
  }

  // If a record has been deleted, discard the payload and replace it with a
  // tombstone
  private static CoursePayload processEvent(CourseEvent event) {
    return event.getAction() == Action.DELETE ? null : event.getPayload();
  }

  private String inputTopic() {
    return config.inputTopics().courses();
  }

  private Serde<CourseEvent> inputSerde() {
    return serdes.specificSerde(CourseEvent.class);
  }

  private String outputTopic() {
    return config.stateStores().courses();
  }

  private Serde<CoursePayload> outputSerde() {
    return serdes.specificSerde(CoursePayload.class);
  }

}
