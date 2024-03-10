package mikaa.feature.score;

import jakarta.transaction.Transactional;

import org.modelmapper.ModelMapper;

import lombok.RequiredArgsConstructor;
import mikaa.api.ScoresApi;
import mikaa.model.ScoreDTO;

@RequiredArgsConstructor
class ScoreResource implements ScoresApi {

  private final ModelMapper mapper;
  private final ScoreService service;

  @Override
  public ScoreDTO getScore(Integer id) {
    var score = service.findOrThrow(id);
    return mapper.map(score, ScoreDTO.class);
  }

  @Override
  @Transactional
  public void deleteScore(Integer id) {
    service.delete(id);
  }

}
