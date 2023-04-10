package mikaa.util;

import java.util.Optional;

public class NameFilter {
  private final Optional<String> name;

  public NameFilter(String name) {
    this.name = Optional.ofNullable(name);
  }

  public String getOrEmpty() {
    return name.orElse("");
  }

}
