package mikaa.infra.streams;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.apache.kafka.streams.state.KeyValueStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import lombok.RequiredArgsConstructor;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.domain.CourseEvent;
import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.kiskotaan.domain.Hole;

@QuarkusTest
@RequiredArgsConstructor
class CoursesTopologyBuilderTest {

  private final CoursesTopologyBuilder coursesTopology;
  private final Topology topology;

  private TopologyTestDriver testDriver;
  private TestInputTopic<Long, CourseEvent> inputTopic;
  private KeyValueStore<Long, CoursePayload> stateStore;

  @BeforeEach
  void init() {
    testDriver = new TopologyTestDriver(topology);

    var input = coursesTopology.description().input();
    inputTopic = testDriver.createInputTopic(
        input.name(),
        input.keySerde().serializer(),
        input.valueSerde().serializer());

    var stateStoreName = coursesTopology.description().output().name();
    stateStore = testDriver.getKeyValueStore(stateStoreName);
  }

  @Test
  void should_have_courses_state_store() {
    var payload = new CoursePayload(1l, "Laajis", List.of(new Hole(2l, 1, 3, 90)));
    inputTopic.pipeInput(1l, new CourseEvent(Action.ADD, payload));

    var course = stateStore.get(1l);
    assertEquals(1l, course.getId());
    assertEquals("Laajis", course.getName());
  }

}
