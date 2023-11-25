package mikaa.streams;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class TopologyProducer {

  private final KafkaStreamsConfig kafkaConfig;
  private final SerdeConfigurer serdes;

  @Produces
  Topology topology() {
    var builder = new StreamsBuilder();

    CoursesTopology.build(builder, kafkaConfig, serdes);

    var topology = builder.build();
    log.info("topology {}", topology.describe());

    return topology;
  }

}
