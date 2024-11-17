package mikaa.feature.scorecard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
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
import mikaa.kiskotaan.scorecard.ScoreEntry;
import mikaa.domain.NewScore;
import mikaa.feature.course.CourseEntity;
import mikaa.feature.course.HoleEntity;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.player.PlayerFinder;
import mikaa.producers.ScoreProducer;

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
  private ScoreCardService scoreCardService;

  @InjectMock
  private PlayerFinder playerFinder;

  private InMemorySink<Record<Long, ScoreEntry>> sink;
  private ScoreService service;

  @BeforeEach
  void setup() {
    service = new ScoreService(playerFinder, scoreCardService, producer, repository);
    sink = connector.sink(ScoreProducer.SCORES_CHANNEL);
    sink.clear();
  }

  @Test
  void sends_event_on_add() {
    when(scoreCardService.findOrThrow(anyLong())).thenReturn(scoreCardMock());
    when(playerFinder.findOrThrow(anyLong())).thenReturn(playerMock());

    service.addScore(13l, new NewScore(2, 1, 4));

    assertEquals(1, sink.received().size());
    var hole1Score = sink.received().get(0).getPayload().value();
    assertEquals(2, hole1Score.getPlayerId());
    assertEquals(13, hole1Score.getScoreCardId());
    assertEquals(1, hole1Score.getHole());
    assertEquals(4, hole1Score.getScore());
  }

  @Test
  void sends_tombstone_event_on_delete() {
    when(repository.deleteById(anyLong())).thenReturn(true);

    service.delete(22);

    assertEquals(1, sink.received().size());
    var record = sink.received().get(0).getPayload();
    assertEquals(22, record.key());
    assertNull(record.value());
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
