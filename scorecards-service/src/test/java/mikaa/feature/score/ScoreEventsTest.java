package mikaa.feature.score;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import mikaa.kiskotaan.domain.ScorePayload;
import mikaa.config.OutgoingChannels;
import mikaa.feature.course.CourseEntity;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.player.PlayerFinder;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.feature.scorecard.ScoreCardFinder;
import mikaa.model.NewScoreDTO;
import mikaa.producers.scorecard.ScoreCardProducer;

@QuarkusTest
class ScoreEventsTest {

  @Any
  @Inject
  private InMemoryConnector connector;

  @Inject
  private ScoreCardProducer producer;

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

    var sink = initSink(OutgoingChannels.SCORECARD_UPDATED);

    var newScore = new NewScoreDTO()
        .hole(9)
        .playerId(BigDecimal.valueOf(2l))
        .score(4);

    service.addScore(13l, newScore);
    // ID is set to 0 because of mocked repository. See stupid hack in ScoreService.fromEntity method.
    assertEvent(sink, new ScorePayload(0l, 2l, 13l, 9, 4));
  }

  @Test
  void sends_event_on_delete() {
    var score = new ScoreEntity(22l, 16, 5, playerMock(), scoreCardMock());
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(score));

    var sink = initSink(OutgoingChannels.SCORECARD_UPDATED);

    service.delete(22);
    assertEvent(sink, new ScorePayload(22l, 2l, 13l, 16, 5));
  }

  private static void assertEvent(InMemorySink<ScorePayload> sink, ScorePayload expected) {
    assertEquals(1, sink.received().size());
    var payload = sink.received().get(0).getPayload();

    assertEquals(expected.getHole(), payload.getHole());
    assertEquals(expected.getPlayerId(), payload.getPlayerId());
    assertEquals(expected.getScore(), payload.getScore());
    assertEquals(expected.getScoreCardId(), payload.getScoreCardId());
  }

  private static CourseEntity courseMock() {
    return new CourseEntity(1l, "Course");
  }

  private static PlayerEntity playerMock() {
    return new PlayerEntity(2l, "Pekka", "Kana");
  }

  private static ScoreCardEntity scoreCardMock() {
    return new ScoreCardEntity(13L, courseMock(), Set.of(playerMock()), new ArrayList<>());
  }

  private InMemorySink<ScorePayload> initSink(String channel) {
    return connector.sink(channel);
  }

}
