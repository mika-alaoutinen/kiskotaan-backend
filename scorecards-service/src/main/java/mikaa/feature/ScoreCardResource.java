package mikaa.feature;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;

import io.smallrye.common.annotation.Blocking;
import lombok.RequiredArgsConstructor;
import mikaa.api.ScoreCardsApi;
import mikaa.errors.NotFoundException;
import mikaa.model.NewScoreCardDTO;
import mikaa.model.ScoreCardDTO;
import mikaa.model.ScoreRowDTO;

@ApplicationScoped
@Blocking
@RequiredArgsConstructor
class ScoreCardResource implements ScoreCardsApi {

  private static final ModelMapper MAPPER = new ModelMapper();
  private final ScoreCardService service;

  @Override
  public ScoreCardDTO addScoreCard(@Valid @NotNull NewScoreCardDTO newScoreCardDTO) {
    return null;
  }

  @Override
  public void deleteScoreCard(Integer id) {
  }

  @Override
  public ScoreCardDTO getScoreCard(Integer id) {
    return service.findOne(id)
        .map(ScoreCardResource::mapScoreCard)
        .orElseThrow(() -> notFound(id));
  }

  @Override
  public List<ScoreCardDTO> getScoreCards() {
    var card = service.findAll().get(0);
    System.out.println(card);

    return service.findAll()
        .stream()
        .map(ScoreCardResource::mapScoreCard)
        .toList();
  }

  @Override
  public ScoreRowDTO updateScores(Integer id, @Valid @NotNull ScoreRowDTO scoreRowDTO) {
    return null;
  }

  private static ScoreCardDTO mapScoreCard(ScoreCardEntity scoreCard) {
    return MAPPER.map(scoreCard, ScoreCardDTO.class);
  }

  private static NotFoundException notFound(int id) {
    return new NotFoundException("Could not find score card with id " + id);
  }

}
