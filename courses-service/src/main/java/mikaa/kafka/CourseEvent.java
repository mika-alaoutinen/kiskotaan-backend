package mikaa.kafka;

import mikaa.dto.CourseDTO;

public record CourseEvent(EventType type, CourseDTO course) {
}
