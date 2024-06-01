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
    assertCourse(LAAJIS_ID, "Laajis", 1);
  }

  @Test
  void should_update_course_in_state_store() {
    assertCourse(LAAJIS_ID, "Laajis", 1);
    inputTopic.pipeInput(LAAJIS_ID, event(Action.UPDATE, LAAJIS_ID, "Updated"));
    assertCourse(LAAJIS_ID, "Updated", 1);
  }

  @Test
  void should_add_hole_to_course() {
    assertCourse(LAAJIS_ID, "Laajis", 1);
    var holes = List.of(new Hole(2l, 1, 3, 90), new Hole(9L, 2, 4, 120));
    inputTopic.pipeInput(LAAJIS_ID, holeEvent(holes));
    assertCourse(LAAJIS_ID, "Laajis", 2);
  }

  @Test
  void should_update_hole() {
    assertCourse(LAAJIS_ID, "Laajis", 1);
    var holes = List.of(new Hole(2l, 1, 5, 220));
    inputTopic.pipeInput(LAAJIS_ID, holeEvent(holes));
    var course = assertCourse(LAAJIS_ID, "Laajis", 1);

    var hole1 = course.getHoles().get(0);
    assertEquals(1, hole1.getNumber());
    assertEquals(5, hole1.getPar());
    assertEquals(220, hole1.getDistance());
  }

  @Test
  void should_delete_course_from_state_store() {
    assertCourse(LAAJIS_ID, "Laajis", 1);
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

  private static CourseEvent holeEvent(List<Hole> holes) {
    var course = new CoursePayload(LAAJIS_ID, "Laajis", holes);
    return new CourseEvent(Action.UPDATE, course);
  }

  private CoursePayload assertCourse(long expectedId, String expectedName, int expectedHoles) {
    var course = stateStore.get(expectedId);
    assertEquals(expectedId, course.getId());
    assertEquals(expectedName, course.getName());
    assertEquals(expectedHoles, course.getHoles().size());
    return course;
  }

}
