package mikaa.events.course;

import java.util.List;

record CoursePayload(long id, String name, List<HolePayload> holes) {
}
