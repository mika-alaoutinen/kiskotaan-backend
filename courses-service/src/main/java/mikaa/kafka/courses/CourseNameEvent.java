package mikaa.kafka.courses;

import mikaa.dto.CourseNameDTO;

public record CourseNameEvent(CourseEventType type, CourseNameDTO payload) {
}
