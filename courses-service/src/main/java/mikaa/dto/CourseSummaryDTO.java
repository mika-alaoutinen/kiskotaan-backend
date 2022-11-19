package mikaa.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record CourseSummaryDTO(long id, String name, int holes, int par) {
}
