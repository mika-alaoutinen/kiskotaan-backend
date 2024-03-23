package mikaa.uni;

import java.util.function.Function;

import io.smallrye.mutiny.Uni;

/**
 * UniItem fixes the ill-behaving mapping functions of Mutiny Uni and adds
 * convenient shortcuts.
 * 
 * Uni mapping functions should behave in a monadic fashion similarly to f. ex.
 * Optional, where they apply the mapping function if the source is not null and
 * do nothing otherwise.
 */
public interface UniItem<T> {

  static <T> UniItem<T> from(Uni<T> uni) {
    return new UniItemImpl<T>(uni);
  }

  static UniItem<Void> empty() {
    return new UniItemImpl<Void>(Uni.createFrom().nullItem());
  }

  <R> UniItem<T> call(Function<? super T, Uni<?>> callback);

  <R> UniItem<R> flatMap(Function<? super T, Uni<? extends R>> mapper);

  <R> UniItem<R> map(Function<? super T, R> mapper);

  Uni<Void> ifPresent(Function<T, Uni<? extends Void>> mapper);

  Uni<Void> discard();

  Uni<T> orThrow(Throwable failure);

  Uni<T> unwrap();

}
