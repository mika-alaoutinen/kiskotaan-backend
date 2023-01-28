package mikaa.events.course;

import java.util.List;

public record CoursePayload(long id, String name, List<Hole> holes) {
}
