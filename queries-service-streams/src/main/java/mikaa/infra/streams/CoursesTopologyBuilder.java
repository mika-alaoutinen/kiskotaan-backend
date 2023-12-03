package mikaa.infra.streams;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.Stores;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.CourseEvent;
import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.streams.KafkaStreamsConfig;

@ApplicationScoped
@RequiredArgsConstructor
class CoursesTopologyBuilder {

  private final KafkaStreamsConfig kafkaConfig;
  private final SerdeConfigurer serdes;

  void build(StreamsBuilder builder) {
    var keySerde = Serdes.Long();

    builder
        .stream(inputTopic(), Consumed.with(keySerde, inputSerde()))
        .mapValues(CourseEvent::getPayload)
        .toTable(Materialized.<Long, CoursePayload>as(Stores.persistentKeyValueStore(outputTopic()))
            .withKeySerde(keySerde)
            .withValueSerde(outputSerde()));
  }

  record CourseTopic<K, V extends SpecificRecord>(
      String topicName,
      Serde<K> keySerde,
      Serde<V> valueSerde) {
  }

  record Description(
      CourseTopic<Long, CourseEvent> input,
      CourseTopic<Long, CoursePayload> output) {
  }

  Description description() {
    var keySerde = Serdes.Long();
    var input = new CourseTopic<>(inputTopic(), keySerde, inputSerde());
    var output = new CourseTopic<>(outputTopic(), keySerde, outputSerde());
    return new Description(input, output);
  }

  private String inputTopic() {
    return kafkaConfig.inputTopics().courses();
  }

  private Serde<CourseEvent> inputSerde() {
    return serdes.specificSerde(CourseEvent.class);
  }

  private String outputTopic() {
    return kafkaConfig.stateStores().courses();
  }

  private Serde<CoursePayload> outputSerde() {
    return serdes.specificSerde(CoursePayload.class);
  }

}
