package mikaa.feature;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "log")
interface LoggingConfig {

    @WithDefault("false")
    boolean enabled();

    @WithDefault("warn")
    String level();

}