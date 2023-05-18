package mikaa.uni;

import java.util.function.Function;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class UniImpl<T> implements UniDecorator<T> {
  private final Uni<T> value;

  @Override
  public <U> UniDecorator<U> flatMap(Function<? super T, Uni<? extends U>> mapper) {
    var result = value.onItem().ifNotNull().transformToUni(mapper);
    return UniDecorator.from(result);
  }

  @Override
  public <R> UniDecorator<R> map(Function<? super T, R> mapper) {
    var result = value.onItem().ifNotNull().transform(mapper);
    return UniDecorator.from(result);
  }

  @Override
  public Uni<Void> ifPresent(Function<T, Uni<? extends Void>> mapper) {
    return value.onItem().ifNotNull().transformToUni(mapper);
  }

  @Override
  public Uni<T> orThrow(Throwable failure) {
    return value.onItem().ifNull().failWith(failure);
  }

  @Override
  public Uni<T> unwrap() {
    return value;
  }

}
