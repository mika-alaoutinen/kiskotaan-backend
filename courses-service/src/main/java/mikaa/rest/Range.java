package mikaa.rest;

import java.util.Optional;

import lombok.Value;

@Value
public class Range<T extends Comparable<T>> {
  private final Optional<T> min;
  private final Optional<T> max;

  public Range(T min, T max) {
    this.min = Optional.ofNullable(min);
    this.max = Optional.ofNullable(max);
  }

  public boolean inRange(T other) {
    var minCond = min.map(x -> x.compareTo(other) <= 0).orElse(true);
    var maxCond = max.map(x -> x.compareTo(other) >= 0).orElse(true);
    return minCond && maxCond;
  }
}
