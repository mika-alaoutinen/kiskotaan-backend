package mikaa.feature;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import io.smallrye.common.annotation.Blocking;

@ApplicationScoped
@Blocking
@Path("/scorecards")
@Produces(MediaType.APPLICATION_JSON)
class ScoreCardResource {

  @GET
  public RestResponse<String> hello() {
    return RestResponse.ok("Hello world");
  }

}
