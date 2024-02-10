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
import mikaa.feature.score.ScoreEntity;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.scorecard.ScoreCardEvent;
import mikaa.kiskotaan.scorecard.ScoreCardGroupedScoresEvent;
import mikaa.kiskotaan.scorecard.ScoreCardPayload;

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
    assertStateEvent(sink, Action.ADD);
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
    assertGroupedByEvent(sink, Action.ADD);
  }

  @Test
  void sends_delete_event_to_kafka_state_topics() {
    InMemorySink<Record<Long, ScoreCardEvent>> sink = connector.sink(OutgoingChannels.SCORECARD_STATE);
    sink.clear();
    producer.scoreCardDeleted(scoreCardMock());
    assertStateEvent(sink, Action.DELETE);
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
    assertGroupedByEvent(sink, Action.DELETE);
  }

  @Test
  void sends_update_event_to_kafka_state_topic() {
    InMemorySink<Record<Long, ScoreCardEvent>> sink = connector.sink(OutgoingChannels.SCORECARD_STATE);
    sink.clear();
    producer.scoreCardUpdated(scoreCardWithScores());

    assertEquals(1, sink.received().size());

    var record = sink.received().getFirst().getPayload();
    assertEquals(13L, record.key());
    assertEquals(Action.UPDATE, record.value().getAction());

    var payload = assertStateEvent(sink, Action.UPDATE);
    assertEquals(1, payload.getResults().size());
    assertEquals(1, payload.getScores().size());
  }

  @Test
  void sends_update_event_to_grouped_by_hole_topic() {
    assertScoreUpdateEvent(OutgoingChannels.SCORECARD_BY_HOLE_STATE, "1");
  }

  @Test
  void sends_update_event_to_grouped_by_player_topic() {
    assertScoreUpdateEvent(OutgoingChannels.SCORECARD_BY_PLAYER_STATE, "2");
  }

  private ScoreCardPayload assertStateEvent(InMemorySink<Record<Long, ScoreCardEvent>> sink, Action action) {
    assertEquals(1, sink.received().size());

    var record = sink.received().getFirst().getPayload();
    assertEquals(13L, record.key());
    assertEquals(action, record.value().getAction());

    var payload = record.value().getPayload();
    assertEquals(1, payload.getPlayerIds().size());

    return payload;
  }

  private void assertGroupedByEvent(InMemorySink<Record<Long, ScoreCardGroupedScoresEvent>> sink, Action action) {
    assertEquals(1, sink.received().size());

    var record = sink.received().getFirst().getPayload();
    assertEquals(13L, record.key());
    assertEquals(action, record.value().getAction());

    var payload = record.value().getPayload();
    assertEquals(1, payload.getPlayerIds().size());
  }

  private void assertScoreUpdateEvent(String channel, String scoreKey) {
    InMemorySink<Record<Long, ScoreCardGroupedScoresEvent>> sink = connector.sink(channel);
    sink.clear();
    producer.scoreCardUpdated(scoreCardWithScores());

    assertEquals(1, sink.received().size());

    var record = sink.received().getFirst().getPayload();
    assertEquals(13L, record.key());
    assertEquals(Action.UPDATE, record.value().getAction());

    var payload = record.value().getPayload();
    assertEquals(1, payload.getResults().size());

    var scoresByPlayer = payload.getScores().get(scoreKey);
    assertEquals(1, scoresByPlayer.size());
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

  private static ScoreCardEntity scoreCardWithScores() {
    var scoreCard = scoreCardMock();
    scoreCard.addScore(new ScoreEntity(1, 4, playerMock()));
    return scoreCard;
  }

}
