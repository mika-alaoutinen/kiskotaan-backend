package mikaa.events.course;

import java.util.List;

record CourseDTO(long id, String name, List<HoleDTO> holes) {
}
