package mikaa.producers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.kafka.Record;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.domain.Course;
import mikaa.domain.Hole;
import mikaa.domain.Player;
import mikaa.domain.Score;
import mikaa.domain.ScoreCard;
import mikaa.feature.scorecard.ScoreCardFinder;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.scorecard.ScoreCardEvent;
import mikaa.kiskotaan.scorecard.ScoreEntry;

@QuarkusTest
class ScoreProcessorTest {

  @InjectMock
  private ScoreCardFinder scoreCardFinder;

  @Any
  @Inject
  private InMemoryConnector connector;

  private InMemorySink<Record<Long, ScoreCardEvent>> sink;
  private InMemorySource<ScoreEntry> source;

  @BeforeEach
  void setup() {
    sink = connector.sink(ScoreCardProducer.SCORECARD_STATE);
    source = connector.source(ScoreProcessor.SCORE_ENTRIES);
    sink.clear();
  }

  @Test
  void sends_score_card_update_event_on_add_score() throws InterruptedException {
    when(scoreCardFinder.findByIdOrThrow(3)).thenReturn(scoreCardMock());

    var scoreEntry = ScoreEntry.newBuilder()
        .setId(1)
        .setPlayerId(2)
        .setScoreCardId(3)
        .setHole(1)
        .setScore(4)
        .build();

    sendEvent(scoreEntry);
    assertEquals(1, sink.received().size());

    var event = sink.received().get(0).getPayload().value();
    assertEquals(Action.UPDATE, event.getAction());

    var scoreCard = event.getPayload();
    assertEquals(1, scoreCard.getScores().size());
  }

  private static ScoreCard scoreCardMock() {
    var holes = List.of(new Hole(1, 4));
    var course = new Course(321, "Laajis", holes);
    var player = new Player(2, "Pekka", "Kana");
    var scores = List.of(new Score(1, player.id(), 1, 4));
    return new ScoreCard(3L, course, Set.of(player), scores);
  }

  private void sendEvent(ScoreEntry score) throws InterruptedException {
    source.send(score);
    // I guess in-memory test channels don't work so great with blocking code
    Thread.sleep(500);
  }

}
