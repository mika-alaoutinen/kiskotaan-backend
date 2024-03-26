package mikaa.uni;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import io.smallrye.mutiny.Uni;

class UniCollectionImpl<T> implements UniCollection<T> {

  private final Uni<Collection<T>> collection;

  UniCollectionImpl(Uni<Collection<T>> collection) {
    this.collection = collection.replaceIfNullWith(Collections.emptyList());
  }

  @Override
  public <U> UniCollection<U> map(Function<? super T, U> mapper) {
    Uni<Collection<U>> result = collection.map(items -> items.stream().map(mapper).toList());
    return UniCollection.from(result);
  }

  @Override
  public Uni<Collection<T>> unwrap() {
    return collection;
  }

}
