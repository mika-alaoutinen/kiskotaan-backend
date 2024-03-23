package mikaa.uni;

import java.util.function.Function;

import io.smallrye.mutiny.Uni;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class UniItemImpl<T> implements UniItem<T> {

  private final Uni<T> value;

  @Override
  public <R> UniItem<R> flatMap(Function<? super T, Uni<? extends R>> mapper) {
    var result = value.onItem().ifNotNull().transformToUni(mapper);
    return UniItem.from(result);
  }

  @Override
  public <R> UniItem<R> map(Function<? super T, R> mapper) {
    var result = value.onItem().ifNotNull().transform(mapper);
    return UniItem.from(result);
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
