package mikaa.feature.scorecard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
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
import mikaa.config.OutgoingChannels;
import mikaa.feature.course.CourseEntity;
import mikaa.feature.course.CourseFinder;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.player.PlayerFinder;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.scorecard.ScoreCardEvent;
import mikaa.model.NewScoreCardDTO;
import mikaa.producers.ScoreCardProducer;

@QuarkusTest
class ScoreCardEventsTest {

  @Any
  @Inject
  private InMemoryConnector connector;

  @Inject
  private ScoreCardProducer producer;

  @InjectMock
  private ScoreCardRepository repository;

  @InjectMock
  private CourseFinder courseFinder;

  @InjectMock
  private PlayerFinder playerFinder;

  private InMemorySink<Record<Long, ScoreCardEvent>> sink;
  private ScoreCardService service;

  @BeforeEach
  void setup() {
    service = new ScoreCardService(courseFinder, playerFinder, producer, repository);
    sink = connector.sink(OutgoingChannels.SCORECARD_STATE);
    sink.clear();
  }

  @Test
  void sends_events_on_add() {
    when(courseFinder.findOrThrow(anyLong())).thenReturn(courseMock());
    when(playerFinder.findOrThrow(anyLong())).thenReturn(playerMock());

    var dto = new NewScoreCardDTO()
        .courseId(BigDecimal.ONE)
        .playerIds(Set.of(BigDecimal.TEN));

    service.add(dto);
    assertEvent(Action.ADD);
  }

  @Test
  void sends_events_on_delete() {
    var scoreCard = new ScoreCardEntity(13L, courseMock(), Set.of(playerMock()), List.of());
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(scoreCard));

    service.delete(13l);
    assertEvent(Action.DELETE);
  }

  private void assertEvent(Action action) {
    assertEquals(1, sink.received().size());

    var record = sink.received().get(0).getPayload();
    assertEquals(action, record.value().getAction());

    var payload = record.value().getPayload();
    assertEquals(payload.getId(), record.key());
    assertEquals(1l, payload.getCourseId());
    assertEquals(1, payload.getPlayerIds().size());
    assertEquals(2l, payload.getPlayerIds().get(0));
    assertTrue(payload.getScores().isEmpty());
  }

  private static CourseEntity courseMock() {
    return new CourseEntity(1l, "Course");
  }

  private static PlayerEntity playerMock() {
    return new PlayerEntity(2L, 2l, "Pekka", "Kana", Set.of(), null);
  }

}
