package mikaa.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;

import io.apicurio.registry.serde.avro.AvroSerde;
import mikaa.kiskotaan.domain.CourseEvent;
import mikaa.kiskotaan.domain.CoursePayload;

interface CoursesTopology {

  static void build(StreamsBuilder builder, KafkaStreamsConfig config) {
    var keySerde = Serdes.Long();
    var inputTopic = config.inputTopics().courses();
    var outputTopic = config.stateStores().courses();

    builder
        .stream(inputTopic, Consumed.with(keySerde, new AvroSerde<CourseEvent>()))
        .mapValues(CourseEvent::getPayload)
        .to(outputTopic, Produced.with(keySerde, new AvroSerde<CoursePayload>()));
  }

}
