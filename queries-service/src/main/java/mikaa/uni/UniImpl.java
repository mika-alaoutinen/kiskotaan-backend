package mikaa.uni;

import java.util.function.Function;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class UniImpl<T> implements UniDecorator<T> {
  private final Uni<T> value;

  @Override
  public UniDecorator<T> flatMap(Function<T, Uni<? extends T>> mapper) {
    var result = value.onItem().ifNotNull().transformToUni(mapper);
    return UniDecorator.from(result);
  }

  @Override
  public UniDecorator<T> map(Function<T, T> mapper) {
    var result = value.onItem().ifNotNull().transform(mapper);
    return UniDecorator.from(result);
  }

  public Uni<T> unwrap() {
    return value;
  }

}
