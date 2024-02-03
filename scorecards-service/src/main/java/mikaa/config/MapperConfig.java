package mikaa.config;

import jakarta.enterprise.context.ApplicationScoped;

import org.modelmapper.ModelMapper;

import mikaa.feature.course.CourseEntity;
import mikaa.feature.player.PlayerEntity;
import mikaa.feature.score.ScoreEntity;
import mikaa.kiskotaan.scorecard.ScoreEntry;
import mikaa.model.CourseDTO;
import mikaa.model.PlayerDTO;

/**
 * Create ModelMapper instance with custom mappings.
 */
class MapperConfig {

  @ApplicationScoped
  ModelMapper modelMapper() {
    var mapper = new ModelMapper();

    // Map entity external id -> DTO id
    mapper.typeMap(CourseEntity.class, CourseDTO.class)
        .addMapping(CourseEntity::getExternalId, CourseDTO::setId);

    // Map entity external id -> DTO id
    mapper.typeMap(PlayerEntity.class, PlayerDTO.class)
        .addMapping(PlayerEntity::getExternalId, PlayerDTO::setId);

    // Map entity external id -> DTO id
    mapper.typeMap(ScoreEntity.class, ScoreEntry.class)
        .addMapping(scoreEntity -> scoreEntity.getPlayer().getExternalId(), ScoreEntry::setPlayerId);

    return mapper;
  }

}
