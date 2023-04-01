package mikaa.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "logging-settings")
public interface LoggingConfig {

    @WithDefault("false")
    public boolean enabled();

    @WithDefault("warn")
    public LogLevel level();

}