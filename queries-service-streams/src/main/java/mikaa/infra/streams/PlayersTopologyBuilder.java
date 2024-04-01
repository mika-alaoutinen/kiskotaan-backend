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
import mikaa.kiskotaan.player.PlayerEvent;
import mikaa.kiskotaan.player.PlayerPayload;
import mikaa.streams.KafkaStreamsConfig;
import mikaa.streams.TopologyDescription.PlayersTopology;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class PlayersTopologyBuilder implements PlayersTopology {

  private final KafkaStreamsConfig config;
  private final SerdeConfigurer serdes;

  @Override
  public Description<Long, PlayerEvent, PlayerPayload> description() {
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
        .filterNot((id, event) -> event.getAction() == Action.UNKNOWN)
        .mapValues(PlayersTopologyBuilder::processEvent)
        .toTable(Materialized.<Long, PlayerPayload>as(Stores.persistentKeyValueStore(outputTopic()))
            .withKeySerde(keySerde)
            .withValueSerde(outputSerde()));
  }

  private static void logEvent(PlayerEvent event) {
    log.info("Received player event: [ action {}, player ID {} ]", event.getAction(), event.getPayload().getId());
  }

  // If a record has been deleted upstream, discard the payload and replace it
  // with a tombstone in the state store
  private static PlayerPayload processEvent(PlayerEvent event) {
    return event.getAction() == Action.DELETE ? null : event.getPayload();
  }

  private String inputTopic() {
    return config.inputTopics().players();
  }

  private Serde<PlayerEvent> inputSerde() {
    return serdes.specificSerde(PlayerEvent.class);
  }

  private String outputTopic() {
    return config.stateStores().players();
  }

  private Serde<PlayerPayload> outputSerde() {
    return serdes.specificSerde(PlayerPayload.class);
  }

}
