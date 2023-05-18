package mikaa.uni;

import java.util.function.Function;

import io.smallrye.mutiny.Uni;

/**
 * UniDecorator fixes the behaviour of ill-behaving mapping functions of Mutiny
 * Uni and adds convenient shortcuts.
 * 
 * Uni mapping functions should behave in a monadic fashion similarly to f. ex.
 * Optional, where they apply the mapping function if the source is not null and
 * do nothing otherwise.
 */
public interface UniDecorator<T> {

  static <T> UniDecorator<T> from(Uni<T> uni) {
    return new UniImpl<T>(uni);
  }

  static UniDecorator<Void> empty() {
    return new UniImpl<Void>(Uni.createFrom().nullItem());
  }

  // Pipe operations
  <U> UniDecorator<U> flatMap(Function<? super T, Uni<? extends U>> mapper);

  <U> UniDecorator<U> map(Function<? super T, U> mapper);

  // Terminal operations
  Uni<Void> ifPresent(Function<T, Uni<? extends Void>> mapper);

  Uni<T> orThrow(Throwable failure);

  Uni<T> unwrap();

}
