package mikaa.dto;

import java.util.List;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record CourseDTO(Long id, String name, List<HoleDTO> holes) {
}
