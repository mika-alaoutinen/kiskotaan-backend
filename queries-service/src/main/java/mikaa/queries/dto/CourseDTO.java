package mikaa.queries.dto;

import java.util.List;

public record CourseDTO(
    long id,
    String name,
    int par,
    List<HoleDTO> holes) {
}
