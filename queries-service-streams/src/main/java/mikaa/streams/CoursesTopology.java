package mikaa.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.Stores;

import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import mikaa.kiskotaan.domain.CourseEvent;
import mikaa.kiskotaan.domain.CoursePayload;

interface CoursesTopology {

  static void build(StreamsBuilder builder, KafkaStreamsConfig config) {
    var keySerde = Serdes.Long();
    var inputTopic = config.inputTopics().courses();
    var outputTopic = config.stateStores().courses();

    builder
        .stream(inputTopic, Consumed.with(keySerde, new ObjectMapperSerde<>(CourseEvent.class)))
        .mapValues(CourseEvent::getPayload)
        .toTable(Materialized.<Long, CoursePayload>as(Stores.persistentKeyValueStore(outputTopic))
            .withKeySerde(keySerde)
            .withValueSerde(new ObjectMapperSerde<>(CoursePayload.class)));
  }

}
