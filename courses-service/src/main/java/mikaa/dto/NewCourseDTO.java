package mikaa.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public record NewCourseDTO(
        @NotBlank @Size(min = 3, max = 40, message = "Course name must be 3-40 chars long") String name,
        @NotNull @Size(min = 1, max = 30, message = "Course can haven 1-30 holes") List<NewHoleDTO> holes) {
}
