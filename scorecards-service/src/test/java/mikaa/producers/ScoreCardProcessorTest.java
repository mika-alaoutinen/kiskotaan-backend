package mikaa.producers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.kafka.Record;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.config.IncomingChannels;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.scorecard.RoundResult;
import mikaa.kiskotaan.scorecard.ScoreCardEvent;
import mikaa.kiskotaan.scorecard.ScoreCardGroupedScoresEvent;
import mikaa.kiskotaan.scorecard.ScoreCardPayload;
import mikaa.kiskotaan.scorecard.ScoreEntry;

@QuarkusTest
class ScoreCardProcessorTest {

  @Any
  @Inject
  private InMemoryConnector connector;

  private InMemorySink<Record<Long, ScoreCardGroupedScoresEvent>> sink;
  private InMemorySource<ScoreCardEvent> source;

  @BeforeEach
  void setup() {
    source = connector.source(IncomingChannels.SCORECARD_STATE);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      ScoreCardProcessor.SCORECARD_BY_HOLE_STATE,
      ScoreCardProcessor.SCORECARD_BY_PLAYER_STATE
  })
  void sends_add_event_to_grouped_score_kafka_topics(String channel) {
    sink = connector.sink(channel);
    sink.clear();
    source.send(new ScoreCardEvent(Action.ADD, payload()));
    assertGroupedByEvent(sink, Action.ADD);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      ScoreCardProcessor.SCORECARD_BY_HOLE_STATE,
      ScoreCardProcessor.SCORECARD_BY_PLAYER_STATE
  })
  void sends_delete_event_to_grouped_score_kafka_topics(String channel) {
    sink = connector.sink(channel);
    sink.clear();
    source.send(new ScoreCardEvent(Action.DELETE, payload()));
    assertGroupedByEvent(sink, Action.DELETE);
  }

  @Test
  void sends_update_event_to_grouped_by_hole_topic() {
    assertScoreUpdateEvent(ScoreCardProcessor.SCORECARD_BY_HOLE_STATE, "1");
  }

  @Test
  void sends_update_event_to_grouped_by_player_topic() {
    assertScoreUpdateEvent(ScoreCardProcessor.SCORECARD_BY_PLAYER_STATE, "2");
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
    sink = connector.sink(channel);
    sink.clear();
    source.send(new ScoreCardEvent(Action.UPDATE, payload()));

    assertEquals(1, sink.received().size());

    var record = sink.received().getFirst().getPayload();
    assertEquals(13L, record.key());
    assertEquals(Action.UPDATE, record.value().getAction());

    var payload = record.value().getPayload();
    assertEquals(1, payload.getResults().size());

    var scoresByPlayer = payload.getScores().get(scoreKey);
    assertEquals(1, scoresByPlayer.size());
  }

  private static ScoreCardPayload payload() {
    var results = Map.of("2", new RoundResult(-1, 3));
    var scores = List.of(new ScoreEntry(10l, 2l, 13l, 1, 3));
    return new ScoreCardPayload(13l, 1l, List.of(2l), results, scores);
  }

}
