package mikaa.feature;

import mikaa.model.ScoreCardDTO;

interface ScoreCardMapper {

  static ScoreCardDTO dto(ScoreCardEntity entity) {
    return new ScoreCardDTO();
  }

}
