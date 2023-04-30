package mikaa.feature.score;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.ScorePayload;
import mikaa.events.OutgoingChannels;
import mikaa.events.score.ScoreProducer;
import mikaa.feature.course.CourseEntity;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.player.PlayerFinder;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.feature.scorecard.ScoreCardFinder;
import mikaa.model.NewScoreDTO;

@QuarkusTest
class ScoreEventsTest {

  @Any
  @Inject
  private InMemoryConnector connector;

  @Inject
  private ScoreProducer producer;

  @InjectMock
  private ScoreRepository repository;

  @InjectMock
  private ScoreCardFinder scoreCardFinder;

  @InjectMock
  private PlayerFinder playerFinder;

  private ScoreService service;

  @BeforeEach
  void setup() {
    service = new ScoreService(playerFinder, scoreCardFinder, producer, repository);
  }

  @Test
  void sends_event_on_add() {
    when(scoreCardFinder.findOrThrow(anyLong())).thenReturn(scoreCardMock());
    when(playerFinder.findOrThrow(anyLong())).thenReturn(playerMock());

    var sink = initSink(OutgoingChannels.Score.SCORE_ADDED);

    var newScore = new NewScoreDTO()
        .hole(9)
        .playerId(BigDecimal.valueOf(2l))
        .score(4);

    service.addScore(13l, newScore);
    assertEvent(sink, new ScorePayload(null, 9, 4, 2l, 13l));
  }

  @Test
  void sends_event_on_delete() {
    var score = new ScoreEntity(22l, 16, 5, playerMock(), scoreCardMock());
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(score));

    var sink = initSink(OutgoingChannels.Score.SCORE_DELETED);

    service.delete(22);
    assertEvent(sink, new ScorePayload(null, 16, 5, 2l, 13l));
  }

  private static void assertEvent(InMemorySink<ScorePayload> sink, ScorePayload expected) {
    assertEquals(1, sink.received().size());
    var payload = sink.received().get(0).getPayload();

    assertEquals(expected.hole(), payload.hole());
    assertEquals(expected.playerId(), payload.playerId());
    assertEquals(expected.score(), payload.score());
    assertEquals(expected.scoreCardId(), payload.scoreCardId());
  }

  private static CourseEntity courseMock() {
    return new CourseEntity(1L, 1l, 18, "Course", null);
  }

  private static PlayerEntity playerMock() {
    return new PlayerEntity(2L, 2l, "Pekka", "Kana", new HashSet<>(), null);
  }

  private static ScoreCardEntity scoreCardMock() {
    return new ScoreCardEntity(13L, courseMock(), Set.of(playerMock()), new ArrayList<>());
  }

  private InMemorySink<ScorePayload> initSink(String channel) {
    return connector.sink(channel);
  }

}
