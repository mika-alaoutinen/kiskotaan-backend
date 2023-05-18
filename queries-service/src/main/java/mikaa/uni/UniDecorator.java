package mikaa.uni;

import java.util.function.Function;

import io.smallrye.mutiny.Uni;

/**
 * UniDecorator fixes the behaviour of ill-behaving mapping functions of Mutiny
 * Uni and adds convenient shortcuts.
 * 
 * Uni mapping functions should behave similarly to f.ex. Optional, where they
 * apply the mapping function if the source is not null and do nothing
 * otherwise.
 */
public interface UniDecorator<T> {

  static <T> UniDecorator<T> from(Uni<T> uni) {
    return new UniImpl<T>(uni);
  }

  UniDecorator<T> flatMap(Function<T, Uni<? extends T>> mapper);

  UniDecorator<T> map(Function<T, T> mapper);

  Uni<T> orThrow(Throwable failure);

  Uni<T> unwrap();

}
