package mikaa.uni;

import java.util.function.Function;

import io.smallrye.mutiny.Uni;

/**
 * Generic helper functions that fix the behaviour of Mutiny Uni methods.
 * 
 * Uni functions should behave similarly to f.ex. Optional, where they apply the
 * mapping function if the source is not null and do nothing otherwise.
 */
public interface UniDecorator<T> {

  static <T> UniDecorator<T> from(Uni<T> uni) {
    return new UniImpl<T>(uni);
  }

  UniDecorator<T> flatMap(Function<T, Uni<? extends T>> mapper);

  UniDecorator<T> map(Function<T, T> mapper);

  Uni<T> unwrap();

}
