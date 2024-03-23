package mikaa.infra;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import liquibase.command.CommandScope;
import liquibase.command.core.UpdateCommandStep;
import liquibase.command.core.helpers.DbUrlConnectionArgumentsCommandStep;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
class LiquibaseRunner {

  @ConfigProperty(name = "quarkus.datasource.reactive.url")
  private String url;

  @ConfigProperty(name = "quarkus.datasource.username")
  private String username;

  @ConfigProperty(name = "quarkus.datasource.password")
  private String password;

  @ConfigProperty(name = "quarkus.liquibase.change-log")
  private String changeLogLocation;

  /**
   * Runs Liquibase migrations on application startup. This is a hack to get
   * Liquibase working with reactive hibernate.
   */
  void onApplicationStart(@Observes StartupEvent event) {
    String jdbcUrl = url.replace("vertx-reactive", "jdbc");

    try {
      new CommandScope("update")
          .addArgumentValue(DbUrlConnectionArgumentsCommandStep.URL_ARG, jdbcUrl)
          .addArgumentValue(DbUrlConnectionArgumentsCommandStep.USERNAME_ARG, username)
          .addArgumentValue(DbUrlConnectionArgumentsCommandStep.PASSWORD_ARG, password)
          .addArgumentValue(UpdateCommandStep.CHANGELOG_FILE_ARG, changeLogLocation)
          .execute();
    } catch (Exception e) {
      log.error("Liquibase Migration Exception: " + e.getStackTrace());
    }
  }

}
