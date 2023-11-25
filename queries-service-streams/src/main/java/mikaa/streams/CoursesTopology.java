package mikaa.streams;

import java.util.HashMap;
import java.util.Map;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.Stores;

import io.apicurio.registry.serde.SerdeConfig;
import io.apicurio.registry.serde.avro.AvroKafkaSerdeConfig;
import io.apicurio.registry.serde.avro.AvroSerde;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import mikaa.kiskotaan.domain.CourseEvent;
import mikaa.kiskotaan.domain.CoursePayload;

interface CoursesTopology {

  static void build(StreamsBuilder builder, KafkaStreamsConfig config) {
    var keySerde = Serdes.Long();

    var inputSerde = inputSerde(CourseEvent.class);
    var inputTopic = config.inputTopics().courses();

    var outputSerde = new ObjectMapperSerde<>(CoursePayload.class);
    var outputTopic = config.stateStores().courses();

    builder
        .stream(inputTopic, Consumed.with(keySerde, inputSerde))
        .mapValues(CourseEvent::getPayload)
        .toTable(Materialized.<Long, CoursePayload>as(Stores.persistentKeyValueStore(outputTopic))
            .withKeySerde(keySerde)
            .withValueSerde(outputSerde));
  }

  private static <T extends SpecificRecord> Serde<T> inputSerde(Class<T> type) {
    var serde = new AvroSerde<T>();
    serde.configure(serdeConfig(), false);
    return serde;
  }

  private static Map<String, Object> serdeConfig() {
    Map<String, Object> config = new HashMap<>();
    config.put(AvroKafkaSerdeConfig.USE_SPECIFIC_AVRO_READER, true);
    config.put(SerdeConfig.REGISTRY_URL, "http://localhost:64673/apis/registry/v2");
    return config;
  }

}
