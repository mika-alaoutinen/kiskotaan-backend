package mikaa.feature.score;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.smallrye.reactive.messaging.kafka.Record;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.domain.RoundResult;
import mikaa.kiskotaan.domain.ScoreCardByHoleEvent;
import mikaa.kiskotaan.domain.ScoreCardByHolePayload;
import mikaa.kiskotaan.domain.ScoreCardByPlayerEvent;
import mikaa.kiskotaan.domain.ScoreCardByPlayerPayload;
import mikaa.config.OutgoingChannels;
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

  private InMemorySink<Record<Long, ScoreCardByHoleEvent>> byHoleSink;
  private InMemorySink<Record<Long, ScoreCardByPlayerEvent>> byPlayerSink;
  private ScoreService service;

  @BeforeEach
  void setup() {
    service = new ScoreService(playerFinder, scoreCardFinder, producer, repository);

    byHoleSink = connector.sink(OutgoingChannels.SCORECARD_BY_HOLE_STATE);
    byHoleSink.clear();

    byPlayerSink = connector.sink(OutgoingChannels.SCORECARD_BY_PLAYER_STATE);
    byPlayerSink.clear();
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

    String playerId = playerMock().getExternalId() + "";
    var byPlayerPayload = assertByPlayerEvent();
    assertResults(byPlayerPayload.getResults(), playerId);

    var scores = byPlayerPayload.getScores().get(playerId);
    assertEquals(1, scores.size());

    var hole1Score = scores.get(0);
    assertEquals(1, hole1Score.getHole());
    assertEquals(4, hole1Score.getScore());

    var byHolePayload = assertByHoleEvent();
    assertResults(byHolePayload.getResults(), playerId);
  }

  @Test
  void sends_event_on_delete() {
    var score = new ScoreEntity(22l, 16, 5, playerMock(), scoreCardMock());
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(score));

    service.delete(22);

    var byPlayerPayload = assertByPlayerEvent();
    assertTrue(byPlayerPayload.getResults().isEmpty());
    assertTrue(byPlayerPayload.getScores().isEmpty());

    var byHolePayload = assertByHoleEvent();
    assertTrue(byHolePayload.getResults().isEmpty());
  }

  private ScoreCardByHolePayload assertByHoleEvent() {
    assertEquals(1, byHoleSink.received().size());

    var record = byHoleSink.received().get(0).getPayload();
    assertEquals(Action.UPDATE, record.value().getAction());

    var payload = record.value().getPayload();
    assertEquals(record.key(), payload.getId());
    assertEquals(scoreCardMock().getId(), payload.getId());
    assertEquals(List.of(playerMock().getExternalId()), payload.getPlayerIds());

    return payload;
  }

  private ScoreCardByPlayerPayload assertByPlayerEvent() {
    assertEquals(1, byPlayerSink.received().size());

    var record = byPlayerSink.received().get(0).getPayload();
    assertEquals(Action.UPDATE, record.value().getAction());

    var payload = record.value().getPayload();
    assertEquals(record.key(), payload.getId());
    assertEquals(scoreCardMock().getId(), payload.getId());
    assertEquals(List.of(playerMock().getExternalId()), payload.getPlayerIds());

    return payload;
  }

  private static void assertResults(Map<String, RoundResult> results, String playerId) {
    var result = results.get(playerId);
    assertEquals(-1, result.getResult());
    assertEquals(4, result.getTotal());
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
