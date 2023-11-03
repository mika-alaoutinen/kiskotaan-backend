package mikaa.streams;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;

import io.apicurio.registry.serde.avro.AvroSerde;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.CourseEvent;

@ApplicationScoped
@RequiredArgsConstructor
class TopologyProducer {

  private static final Serde<Long> KEY_SERDE = Serdes.Long();
  private final KafkaStreamsConfig config;

  Topology topology() {
    var builder = new StreamsBuilder();

    courseTopics(builder);

    return builder.build();
  }

  private void courseTopics(StreamsBuilder builder) {
    var inputTopic = config.inputTopics().courses();
    var outputTopic = config.stateStores().courses();
    var valueSerde = new AvroSerde<CourseEvent>();

    builder
        .stream(inputTopic, Consumed.with(KEY_SERDE, valueSerde))
        .to(outputTopic, Produced.with(KEY_SERDE, valueSerde));
  }

}
