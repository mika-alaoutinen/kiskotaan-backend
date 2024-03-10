package mikaa.domain;

import java.util.Collection;

public record Course(long id, String name, Collection<Hole> holes) {
}