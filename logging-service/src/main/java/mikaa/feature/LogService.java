package mikaa.feature;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
class LogService {

  private final LoggingConfig config;

  LogService(LoggingConfig config) {
    this.config = config;
  }

  String test() {
    return "logs enabled " + config.enabled() + ", log level " + config.level();
  }

}
