package mikaa.uni;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import io.smallrye.mutiny.Uni;

public interface UniCollection<T> {

  static <T> UniCollection<T> from(Uni<Collection<T>> uni) {
    return new UniCollectionImpl<T>(uni);
  }

  static <T> UniCollection<T> empty() {
    return new UniCollectionImpl<>(Uni.createFrom().item(Collections.emptyList()));
  }

  <U> UniCollection<U> map(Function<? super T, U> mapper);

  Uni<Collection<T>> unwrap();

}
