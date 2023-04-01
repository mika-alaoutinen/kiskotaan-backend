package mikaa.feature;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "log")
public interface LoggingConfig {

    @WithDefault("false")
    public boolean enabled();

    @WithDefault("warn")
    public LogLevel level();

}