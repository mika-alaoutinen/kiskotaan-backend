package mikaa.feature;

import jakarta.enterprise.context.ApplicationScoped;
import mikaa.config.LoggingConfig;

@ApplicationScoped
class LogService {

  private final LoggingConfig config;

  LogService(LoggingConfig config) {
    this.config = config;
  }

  String getLogSettings() {
    String logsEnabled = config.enabled() ? "Logging enabled" : "Logging disabled";
    return String.format("%s, log level %s", logsEnabled, config.level());
  }

}
