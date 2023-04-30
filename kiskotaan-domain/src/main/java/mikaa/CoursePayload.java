package mikaa;

import java.util.List;

public record CoursePayload(Long id, String name, List<Hole> holes) {
}
