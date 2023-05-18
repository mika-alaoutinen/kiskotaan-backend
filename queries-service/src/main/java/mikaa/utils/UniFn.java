package mikaa.utils;

import java.util.function.Function;

import io.smallrye.mutiny.Uni;

/**
 * Generic helper functions that fix the behaviour of Mutiny Uni methods.
 * 
 * Uni functions should behave similarly to f.ex. Optional, where they apply the
 * mapping function if the source is not null and do nothing otherwise.
 */
public interface UniFn<T> {

  static <T> Uni<T> flatMap(Uni<T> source, Function<T, Uni<? extends T>> mapper) {
    return source.onItem().ifNotNull().transformToUni(mapper);
  }

  static <T> Uni<T> map(Uni<T> source, Function<T, ? extends T> mapper) {
    return source.onItem().ifNotNull().transform(mapper);
  }

}
