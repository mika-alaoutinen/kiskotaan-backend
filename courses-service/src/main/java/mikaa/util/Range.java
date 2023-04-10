package mikaa.util;

import java.util.Optional;

import jakarta.annotation.Nullable;
import lombok.Value;

@Value
public class Range<T extends Comparable<T>> {
  private final Optional<T> min;
  private final Optional<T> max;

  public Range(@Nullable T min, @Nullable T max) {
    this.min = Optional.ofNullable(min);
    this.max = Optional.ofNullable(max);
  }

  public boolean within(T other) {
    var minCondition = min.map(x -> x.compareTo(other) <= 0).orElse(true);
    var maxCondition = max.map(x -> x.compareTo(other) >= 0).orElse(true);
    return minCondition && maxCondition;
  }
}
