package mikaa.streams;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;

import io.apicurio.registry.serde.avro.AvroSerde;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.kiskotaan.domain.CourseEvent;
import mikaa.kiskotaan.domain.CoursePayload;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class TopologyProducer {

  private static final Serde<Long> KEY_SERDE = Serdes.Long();
  private final KafkaStreamsConfig config;

  @Produces
  Topology topology() {
    var builder = new StreamsBuilder();

    courseTopics(builder);

    var topology = builder.build();
    log.info("topology {}", topology.describe());

    return topology;
  }

  private void courseTopics(StreamsBuilder builder) {
    var inputTopic = config.inputTopics().courses();
    var outputTopic = config.stateStores().courses();

    builder
        .stream(inputTopic, Consumed.with(KEY_SERDE, new AvroSerde<CourseEvent>()))
        .mapValues(CourseEvent::getPayload)
        .to(outputTopic, Produced.with(KEY_SERDE, new AvroSerde<CoursePayload>()));
  }

}
