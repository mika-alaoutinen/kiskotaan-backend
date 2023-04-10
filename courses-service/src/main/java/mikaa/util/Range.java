package mikaa.util;

import java.util.Optional;
import java.util.function.Predicate;

import jakarta.annotation.Nullable;
import lombok.Value;

// This is an overly complicated way of comparing two integers, but I like it.
@Value
public class Range<T extends Comparable<T>> {
  private final Optional<T> min;
  private final Optional<T> max;

  public Range(@Nullable T min, @Nullable T max) {
    this.min = Optional.ofNullable(min);
    this.max = Optional.ofNullable(max);
  }

  public boolean within(T other) {
    Predicate<Integer> gte = i -> i >= 0;
    Predicate<Integer> lte = i -> i <= 0;
    return compare(min, other, gte) && compare(max, other, lte);
  }

  private boolean compare(Optional<T> value, T other, Predicate<Integer> predicate) {
    return value.map(other::compareTo).map(predicate::test).orElse(true);
  }

}
