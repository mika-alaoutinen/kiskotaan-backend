package mikaa.domain;

import java.util.Collection;

public record NewCourse(String name, Collection<NewHole> holes) {
}
