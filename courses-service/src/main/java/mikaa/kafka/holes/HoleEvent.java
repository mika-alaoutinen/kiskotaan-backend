package mikaa.kafka.holes;

import mikaa.dto.HoleDTO;

public record HoleEvent(HoleEventType type, HoleDTO payload) {
}
