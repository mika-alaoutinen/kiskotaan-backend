package mikaa.feature.scorecard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.events.scorecard.ScoreCardEvent;
import mikaa.events.scorecard.ScoreCardProducer;
import mikaa.feature.course.CourseEntity;
import mikaa.feature.course.CourseFinder;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.player.PlayerFinder;
import mikaa.model.NewScoreCardDTO;

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

  private InMemorySink<ScoreCardEvent> sink;
  private ScoreCardService service;

  @BeforeEach
  void setup() {
    sink = connector.sink("scorecards-out");
    sink.clear();
    service = new ScoreCardService(courseFinder, playerFinder, producer, repository);
  }

  @AfterEach
  void teardown() {
    sink.clear();
  }

  @Test
  void sends_event_on_add() {
    when(courseFinder.findOrThrow(anyLong())).thenReturn(courseMock());
    when(playerFinder.findOrThrow(anyLong())).thenReturn(playerMock());

    var dto = new NewScoreCardDTO()
        .courseId(BigDecimal.ONE)
        .playerIds(Set.of(BigDecimal.TEN));

    service.add(dto);
    assertEvent("SCORECARD_ADDED");
  }

  @Test
  void sends_event_on_delete() {
    var scoreCard = new ScoreCardEntity(13L, courseMock(), Set.of(playerMock()), List.of());
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(scoreCard));

    service.delete(13l);
    assertEvent("SCORECARD_DELETED");
  }

  private void assertEvent(String eventType) {
    var event = sink.received().get(0).getPayload();
    assertEquals(eventType, event.type().toString());

    var payload = event.payload();
    assertEquals(1l, payload.courseId());
    assertEquals(1, payload.playerIds().size());
    assertEquals(2l, payload.playerIds().get(0));
  }

  private static CourseEntity courseMock() {
    return new CourseEntity(1L, 1l, 18, "Course", null);
  }

  private static PlayerEntity playerMock() {
    return new PlayerEntity(2L, 2l, "Pekka", "Kana", Set.of(), null);
  }

}
