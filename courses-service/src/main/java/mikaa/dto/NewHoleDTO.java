package mikaa.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public record NewHoleDTO(
    @Min(1) @Max(30) int number,
    @Min(2) @Max(6) int par,
    @Min(1) int distance) {
}
