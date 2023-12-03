package mikaa.infra.streams;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
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
class CoursesTopologyTest {

  private final CoursesTopologyBuilder coursesTopology;
  private final Topology topology;

  private TopologyTestDriver testDriver;
  private TestInputTopic<Long, CourseEvent> inputTopic;
  private TestOutputTopic<Long, CoursePayload> outputTopic;

  @BeforeEach
  void init() {
    testDriver = new TopologyTestDriver(topology);

    var input = coursesTopology.description().input();
    inputTopic = testDriver.createInputTopic(
        input.topicName(),
        input.keySerde().serializer(),
        input.valueSerde().serializer());

    var output = coursesTopology.description().output();
    outputTopic = testDriver.createOutputTopic(
        output.topicName(),
        output.keySerde().deserializer(),
        output.valueSerde().deserializer());
  }

  @Test
  void should_have_courses_state_store() {
    var course = new CoursePayload(1l, "Laajis", List.of(new Hole(2l, 1, 3, 90)));
    inputTopic.pipeInput(1l, new CourseEvent(Action.ADD, course));

    var message = outputTopic.readKeyValue();
    assertEquals(1l, message.key);
    assertEquals("Laajis", message.value.getName());
    assertTrue(outputTopic.isEmpty());
  }

}
