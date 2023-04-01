package mikaa.feature;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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
    return RestResponse.ok(service.findOne(id));
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Transactional
  public RestResponse<HoleDTO> updateHole(@PathParam("id") Long id, @Valid NewHoleDTO hole) {
    return RestResponse.ok(service.update(id, hole));
  }

  @DELETE
  @Transactional
  public RestResponse<Void> deleteHole(@PathParam("id") Long id) {
    service.delete(id);
    return RestResponse.noContent();
  }

}
