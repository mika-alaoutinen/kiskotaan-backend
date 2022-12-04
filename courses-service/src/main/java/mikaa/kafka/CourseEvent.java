package mikaa.kafka;

import mikaa.dto.CourseDTO;

public record CourseEvent(CourseEventType type, CourseDTO course) {
}
