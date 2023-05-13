package mikaa.consumers.course;

import io.smallrye.mutiny.Uni;
import mikaa.HolePayload;
import mikaa.feature.course.HoleEntity;

public interface HoleWriter {

  Uni<HoleEntity> add(HolePayload payload);

  Uni<HoleEntity> update(HolePayload payload);

  Uni<Void> delete(HolePayload payload);

}
