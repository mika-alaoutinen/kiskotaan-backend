package mikaa.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record CourseSummaryDTO(Long id, String name, int holes, int par) {
}
