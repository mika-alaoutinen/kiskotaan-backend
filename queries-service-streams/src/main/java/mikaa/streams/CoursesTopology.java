package mikaa.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.Stores;

import mikaa.kiskotaan.domain.CourseEvent;
import mikaa.kiskotaan.domain.CoursePayload;

interface CoursesTopology {

  static void build(StreamsBuilder builder, KafkaStreamsConfig kafkaConfig, SerdeConfigurer serdes) {
    var keySerde = Serdes.Long();

    var inputSerde = serdes.specificSerde(CourseEvent.class);
    var inputTopic = kafkaConfig.inputTopics().courses();

    var outputSerde = serdes.specificSerde(CoursePayload.class);
    var outputTopic = kafkaConfig.stateStores().courses();

    builder
        .stream(inputTopic, Consumed.with(keySerde, inputSerde))
        .mapValues(CourseEvent::getPayload)
        .toTable(Materialized.<Long, CoursePayload>as(Stores.persistentKeyValueStore(outputTopic))
            .withKeySerde(keySerde)
            .withValueSerde(outputSerde));
  }

}
