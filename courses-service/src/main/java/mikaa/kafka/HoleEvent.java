package mikaa.kafka;

import mikaa.dto.HoleDTO;

public record HoleEvent(HoleEventType type, HoleDTO hole) {
}
