package mikaa.util;

import java.util.Optional;

import jakarta.annotation.Nullable;

public class StringFilter {
  private final Optional<String> maybeString;

  public StringFilter(@Nullable String s) {
    this.maybeString = Optional.ofNullable(s);
  }

  public boolean contains(String other) {
    return other.contains(maybeString.orElse(""));
  }

}
