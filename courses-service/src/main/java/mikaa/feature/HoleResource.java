package mikaa.feature;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
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

@ApplicationScoped
@Blocking
@Path("/holes/{id}")
@Produces(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor
public class HoleResource {

  private final HoleService service;

  @GET
  public RestResponse<HoleDTO> getHole(@PathParam("id") Long id) {
    return service.findOne(id)
        .map(RestResponse::ok)
        .orElseThrow(() -> notFound(id));
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  public RestResponse<HoleDTO> updateHole(@PathParam("id") Long id, @Valid NewHoleDTO hole) {
    return service.update(id, hole)
        .map(RestResponse::ok)
        .orElseThrow(() -> notFound(id));
  }

  @DELETE
  @Transactional
  public RestResponse<Void> deleteHole(@PathParam("id") Long id) {
    service.delete(id);
    return RestResponse.noContent();
  }

  private static NotFoundException notFound(long id) {
    String msg = "Could not find hole with id " + id;
    return new NotFoundException(msg);
  }

}
