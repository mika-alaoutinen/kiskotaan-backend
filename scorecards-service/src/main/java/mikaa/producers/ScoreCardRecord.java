package mikaa.producers;

import mikaa.feature.scorecard.ScoreCardEntity;
import mikaa.kiskotaan.domain.Action;

record ScoreCardRecord(Action action, ScoreCardEntity entity) {
}
