package mikaa.feature.score;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.scorecard.ScoreCardEvent;
import mikaa.kiskotaan.scorecard.ScoreCardPayload;
import mikaa.feature.course.CourseEntity;
import mikaa.feature.course.HoleEntity;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.player.PlayerFinder;
import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.feature.scorecard.ScoreCardFinder;
import mikaa.model.NewScoreDTO;
import mikaa.producers.ScoreCardProducer;

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

  private InMemorySink<ScoreCardEvent> sink;
  private ScoreService service;

  @BeforeEach
  void setup() {
    service = new ScoreService(playerFinder, scoreCardFinder, producer, repository);
    sink = connector.sink(ScoreCardProducer.INTERNAL_SCORECARD_CHANNEL);
    sink.clear();
  }

  @Test
  void sends_event_on_add() {
    when(scoreCardFinder.findOrThrow(anyLong())).thenReturn(scoreCardMock());
    when(playerFinder.findOrThrow(anyLong())).thenReturn(playerMock());

    var newScore = new NewScoreDTO()
        .hole(1)
        .playerId(BigDecimal.valueOf(2l))
        .score(4);

    service.addScore(13l, newScore);

    var payload = assertEvent();
    assertEquals(1, payload.getScores().size());

    var hole1Score = payload.getScores().get(0);
    assertEquals(2, hole1Score.getPlayerId());
    assertEquals(1, hole1Score.getHole());
    assertEquals(5, hole1Score.getPar());
    assertEquals(4, hole1Score.getScore());

    String playerId = playerMock().getExternalId() + "";
    var result = payload.getResults().get(playerId);
    assertEquals(-1, result.getResult());
    assertEquals(4, result.getTotal());
  }

  @Test
  void sends_event_on_delete() {
    var score = new ScoreEntity(22l, 16, 5, playerMock(), scoreCardMock());
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(score));

    service.delete(22);

    var payload = assertEvent();
    assertTrue(payload.getResults().isEmpty());
    assertTrue(payload.getScores().isEmpty());
  }

  private ScoreCardPayload assertEvent() {
    assertEquals(1, sink.received().size());

    var record = sink.received().get(0).getPayload();
    assertEquals(Action.UPDATE, record.getAction());

    var payload = record.getPayload();
    assertEquals(scoreCardMock().getId(), payload.getId());
    assertEquals(List.of(playerMock().getExternalId()), payload.getPlayerIds());

    return payload;
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
