package mikaa.util;

import java.util.Optional;

import jakarta.annotation.Nullable;

public class StringFilter {
  private final Optional<String> maybeString;

  public StringFilter(@Nullable String s) {
    this.maybeString = Optional.ofNullable(s).map(String::toLowerCase);
  }

  public boolean contains(String other) {
    return other.toLowerCase().contains(maybeString.orElse(""));
  }

}
