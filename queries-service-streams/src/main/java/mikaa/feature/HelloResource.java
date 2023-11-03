package mikaa.feature;

import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@GraphQLApi
public class HelloResource {

  @Query
  public Uni<String> hello() {
    return Uni.createFrom().item("Hello world");
  }

}
