package mikaa.feature;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/logging")
public class LoggingResource {

  private final LogService service;

  public LoggingResource(LogService service) {
    this.service = service;
  }

  @GET
  @Produces(MediaType.TEXT_PLAIN)
  public String logLevel() {
    return service.getLogSettings();
  }

}
