package mikaa.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewCourseDTO(
                @NotBlank @Size(min = 3, max = 40, message = "Course name must be 3-40 chars long") String name,
                @NotNull @Size(min = 1, max = 30, message = "Course can haven 1-30 holes") List<NewHoleDTO> holes) {
}
