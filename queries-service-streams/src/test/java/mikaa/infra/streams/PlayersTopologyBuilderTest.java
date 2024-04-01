package mikaa.infra.streams;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.state.KeyValueStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.player.PlayerEvent;
import mikaa.kiskotaan.player.PlayerPayload;
import mikaa.streams.TopologyDescription.PlayersTopology;

@QuarkusTest
@RequiredArgsConstructor
class PlayersTopologyBuilderTest {

  private static final long AKU_ID = 1;

  private final PlayersTopology playersTopology;
  private final Topology topology;

  private TopologyTestDriver testDriver;
  private TestInputTopic<Long, PlayerEvent> inputTopic;
  private KeyValueStore<Long, PlayerPayload> stateStore;

  @BeforeEach
  void init() {
    testDriver = new TopologyTestDriver(topology);

    var input = playersTopology.description().input();
    inputTopic = testDriver.createInputTopic(
        input.name(),
        input.keySerde().serializer(),
        input.valueSerde().serializer());

    var stateStoreName = playersTopology.description().output().name();
    stateStore = testDriver.getKeyValueStore(stateStoreName);

    // Add Aku Ankka to state store
    inputTopic.pipeInput(AKU_ID, event(Action.ADD, AKU_ID, "Aku", "Ankka"));
  }

  @Test
  void should_add_aku_to_state_store() {
    assertPlayer(AKU_ID, "Aku", "Ankka");
  }

  @Test
  void should_update_player_in_state_store() {
    assertPlayer(AKU_ID, "Aku", "Ankka");
    inputTopic.pipeInput(AKU_ID, event(Action.UPDATE, AKU_ID, "Donald", "Duck"));
    assertPlayer(AKU_ID, "Donald", "Duck");
  }

  @Test
  void should_delete_course_from_state_store() {
    assertPlayer(AKU_ID, "Aku", "Ankka");
    inputTopic.pipeInput(AKU_ID, event(Action.DELETE, AKU_ID, "Aku", "Ankka"));
    assertNull(stateStore.get(AKU_ID));
  }

  @Test
  void should_ignore_unknown_event_types() {
    long id = 4;
    inputTopic.pipeInput(id, event(Action.UNKNOWN, id, "Invalid", "Player"));
    assertNull(stateStore.get(id));
  }

  private static PlayerEvent event(Action action, long id, String firstName, String lastName) {
    var player = new PlayerPayload(id, firstName, lastName);
    return new PlayerEvent(action, player);
  }

  private void assertPlayer(long id, String firstName, String lastName) {
    var player = stateStore.get(id);
    assertEquals(id, player.getId());
    assertEquals(firstName, player.getFirstName());
    assertEquals(lastName, player.getLastName());
  }

}
