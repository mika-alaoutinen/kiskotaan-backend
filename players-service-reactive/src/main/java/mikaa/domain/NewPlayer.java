package mikaa.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewPlayer(
    @NotNull @Size(min = 1, max = 40) String firstName,
    @NotNull @Size(min = 1, max = 40) String lastName) {
}
