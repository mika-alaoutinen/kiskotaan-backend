package mikaa.infra.streams;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.TestInputTopic;
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
import mikaa.streams.KafkaStreamsConfig;

@QuarkusTest
@RequiredArgsConstructor
class CoursesTopologyBuilderTest {

  private final KafkaStreamsConfig kafkaConfig;
  private final SerdeConfigurer serdes;

  private TopologyTestDriver testDriver;
  private TestInputTopic<Long, CourseEvent> inputTopic;
  private KeyValueStore<Long, CoursePayload> stateStore;

  @BeforeEach
  void init() {
    var builder = new StreamsBuilder();
    var coursesTopology = new CoursesTopologyBuilder(kafkaConfig, serdes);
    coursesTopology.build(builder);
    testDriver = new TopologyTestDriver(builder.build());

    var input = coursesTopology.description().input();
    inputTopic = testDriver.createInputTopic(
        input.topicName(),
        input.keySerde().serializer(),
        input.valueSerde().serializer());

    var stateStoreName = coursesTopology.description().output().topicName();
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
