package mikaa.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public record CourseNameDTO(
        Long id,
        @NotBlank(message = "Course name is required") @Size(min = 3, max = 40, message = "Course name must be 3-40 chars long") String name) {
}
