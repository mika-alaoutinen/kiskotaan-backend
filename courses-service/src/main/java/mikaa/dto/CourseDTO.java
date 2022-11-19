package mikaa.dto;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record CourseDTO(long id, String name, List<HoleDTO> holes) {
}
