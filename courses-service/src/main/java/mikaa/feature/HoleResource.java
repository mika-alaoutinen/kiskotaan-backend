package mikaa.feature;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.RestResponse;

import io.smallrye.common.annotation.Blocking;
import lombok.RequiredArgsConstructor;
import mikaa.dto.HoleDTO;
import mikaa.dto.NewHoleDTO;
import mikaa.errors.NotFoundException;

@ApplicationScoped
@Blocking
@Path("/holes")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class HoleResource {

  private final HoleService service;

  @PUT
  @Path("/{id}")
  @Transactional
  public RestResponse<HoleDTO> updateHole(@PathParam("id") Long id, @Valid NewHoleDTO hole) {
    var updatedHole = service.update(id, hole).orElseThrow(() -> notFound(id));
    return RestResponse.ok(updatedHole);
  }

  private static NotFoundException notFound(long id) {
    String msg = "Could not find hole with id " + id;
    return new NotFoundException(msg);
  }

}
