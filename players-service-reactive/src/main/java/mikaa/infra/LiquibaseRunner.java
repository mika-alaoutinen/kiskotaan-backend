package mikaa.infra;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionArgumentsCommandStep;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
class LiquibaseRunner {

  @ConfigMapping(prefix = "quarkus.datasource")
  private interface Datasource {

    @WithName("reactive.url")
    String url();

    String username();

    String password();
  }

  @ConfigProperty(name = "quarkus.liquibase.change-log")
  private String changeLogLocation;

  /**
   * Runs Liquibase migrations on application startup. This is a hack to get
   * Liquibase working with reactive hibernate.
   */
  void onApplicationStart(@Observes StartupEvent event, Datasource ds) {
    String url = ds.url().replace("vertx-reactive", "jdbc");

    try {
      new CommandScope("update")
          .addArgumentValue(DbUrlConnectionArgumentsCommandStep.URL_ARG, url)
          .addArgumentValue(DbUrlConnectionArgumentsCommandStep.USERNAME_ARG, ds.username())
          .addArgumentValue(DbUrlConnectionArgumentsCommandStep.PASSWORD_ARG, ds.password())
          .addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, changeLogLocation)
          .execute();
    } catch (Exception e) {
      log.error("Liquibase Migration Exception: " + e.getStackTrace());
    }
  }

}
