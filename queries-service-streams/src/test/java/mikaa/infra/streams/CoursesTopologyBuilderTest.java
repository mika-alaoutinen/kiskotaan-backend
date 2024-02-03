package mikaa.infra.streams;

import static org.junit.Assert.assertNull;
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
import mikaa.kiskotaan.course.CourseEvent;
import mikaa.kiskotaan.course.CoursePayload;
import mikaa.kiskotaan.course.Hole;
import mikaa.streams.TopologyDescription.CoursesTopology;

@QuarkusTest
@RequiredArgsConstructor
class CoursesTopologyBuilderTest {

  private static final long LAAJIS_ID = 1;

  private final CoursesTopology coursesTopology;
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

    // Add Laajis to state store
    inputTopic.pipeInput(LAAJIS_ID, event(Action.ADD, LAAJIS_ID, "Laajis"));
  }

  @Test
  void should_add_laajis_to_state_store() {
    assertCourse(LAAJIS_ID, "Laajis");
  }

  @Test
  void should_update_course_in_state_store() {
    assertCourse(LAAJIS_ID, "Laajis");
    inputTopic.pipeInput(LAAJIS_ID, event(Action.UPDATE, LAAJIS_ID, "Updated"));
    assertCourse(LAAJIS_ID, "Updated");
  }

  @Test
  void should_delete_course_from_state_store() {
    assertCourse(LAAJIS_ID, "Laajis");
    inputTopic.pipeInput(LAAJIS_ID, event(Action.DELETE, LAAJIS_ID, "Deleted"));
    assertNull(stateStore.get(LAAJIS_ID));
  }

  @Test
  void should_ignore_unknown_event_types() {
    long id = 4;
    inputTopic.pipeInput(id, event(Action.UNKNOWN, id, "Invalid"));
    assertNull(stateStore.get(id));
  }

  private static CourseEvent event(Action action, long id, String name) {
    var course = new CoursePayload(id, name, List.of(new Hole(2l, 1, 3, 90)));
    return new CourseEvent(action, course);
  }

  private void assertCourse(long expectedId, String expectedName) {
    var course = stateStore.get(expectedId);
    assertEquals(expectedId, course.getId());
    assertEquals(expectedName, course.getName());
  }

}
