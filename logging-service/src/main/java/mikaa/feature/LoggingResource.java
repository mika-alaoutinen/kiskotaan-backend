package mikaa.feature;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
