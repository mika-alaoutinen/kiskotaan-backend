package mikaa.utils;

import java.util.function.Function;

import io.smallrye.mutiny.Uni;

public class UniDecorator<T> {
  private final Uni<T> value;

  private UniDecorator(Uni<T> value) {
    this.value = value;
  }

  public static <T> UniDecorator<T> from(Uni<T> uni) {
    return new UniDecorator<T>(uni);
  }

  public UniDecorator<T> flatMap(Function<T, Uni<? extends T>> mapper) {
    var result = value.onItem().ifNotNull().transformToUni(mapper);
    return UniDecorator.from(result);
  }

  public UniDecorator<T> map(Function<T, T> mapper) {
    var result = value.onItem().ifNotNull().transform(mapper);
    return UniDecorator.from(result);
  }

  public Uni<T> toUni() {
    return value;
  }

}
