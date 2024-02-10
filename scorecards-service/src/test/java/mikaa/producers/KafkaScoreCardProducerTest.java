package mikaa.producers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.kafka.Record;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.config.OutgoingChannels;
import mikaa.feature.course.CourseEntity;
import mikaa.feature.course.HoleEntity;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.scorecard.ScoreCardEvent;
import mikaa.kiskotaan.scorecard.ScoreCardGroupedScoresEvent;

@QuarkusTest
class KafkaScoreCardProducerTest {

  @Any
  @Inject
  private InMemoryConnector connector;

  @Inject
  private ScoreCardProducer producer;

  @Test
  void sends_add_event_to_kafka_state_topic() {
    InMemorySink<Record<Long, ScoreCardEvent>> sink = connector.sink(OutgoingChannels.SCORECARD_STATE);
    sink.clear();
    producer.scoreCardAdded(scoreCardMock());

    assertEquals(1, sink.received().size());

    var record = sink.received().getFirst().getPayload();
    assertEquals(13L, record.key());
    assertEquals(Action.ADD, record.value().getAction());

    var payload = record.value().getPayload();
    assertEquals(1, payload.getPlayerIds().size());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      OutgoingChannels.SCORECARD_BY_HOLE_STATE,
      OutgoingChannels.SCORECARD_BY_PLAYER_STATE
  })
  void sends_add_event_to_grouped_score_kafka_topics(String channel) {
    InMemorySink<Record<Long, ScoreCardGroupedScoresEvent>> sink = connector.sink(channel);
    sink.clear();
    producer.scoreCardAdded(scoreCardMock());

    assertEquals(1, sink.received().size());

    var record = sink.received().getFirst().getPayload();
    assertEquals(13L, record.key());
    assertEquals(Action.ADD, record.value().getAction());

    var payload = record.value().getPayload();
    assertEquals(1, payload.getPlayerIds().size());
  }

  @Test
  void sends_delete_event_to_kafka_state_topics() {
    InMemorySink<Record<Long, ScoreCardEvent>> sink = connector.sink(OutgoingChannels.SCORECARD_STATE);
    sink.clear();
    producer.scoreCardDeleted(scoreCardMock());

    assertEquals(1, sink.received().size());

    var record = sink.received().getFirst().getPayload();
    assertEquals(13L, record.key());
    assertEquals(Action.DELETE, record.value().getAction());

    var payload = record.value().getPayload();
    assertEquals(1, payload.getPlayerIds().size());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      OutgoingChannels.SCORECARD_BY_HOLE_STATE,
      OutgoingChannels.SCORECARD_BY_PLAYER_STATE
  })
  void sends_delete_event_to_grouped_score_kafka_topics(String channel) {
    InMemorySink<Record<Long, ScoreCardGroupedScoresEvent>> sink = connector.sink(channel);
    sink.clear();
    producer.scoreCardDeleted(scoreCardMock());

    assertEquals(1, sink.received().size());

    var record = sink.received().getFirst().getPayload();
    assertEquals(13L, record.key());
    assertEquals(Action.DELETE, record.value().getAction());

    var payload = record.value().getPayload();
    assertEquals(1, payload.getPlayerIds().size());
  }

  private static CourseEntity courseMock() {
    return new CourseEntity(1l, List.of(new HoleEntity(1, 5)), "Course");
  }

  private static PlayerEntity playerMock() {
    return new PlayerEntity(2l, "Pekka", "Kana");
  }

  private static ScoreCardEntity scoreCardMock() {
    return new ScoreCardEntity(13L, courseMock(), Set.of(playerMock()), new ArrayList<>());
  }

}
