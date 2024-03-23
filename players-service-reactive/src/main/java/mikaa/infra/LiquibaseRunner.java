package mikaa.infra;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.StartupEvent;
import io.quarkus.runtime.util.ExceptionUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@Slf4j
class LiquibaseRunner {

  @ConfigProperty(name = "quarkus.datasource.reactive.url")
  String url;

  @ConfigProperty(name = "quarkus.datasource.username")
  String username;

  @ConfigProperty(name = "quarkus.datasource.password")
  String password;

  @ConfigProperty(name = "quarkus.liquibase.change-log")
  String changeLogLocation;

  void onApplicationStart(@Observes StartupEvent event) {
    if (LaunchMode.current() != LaunchMode.TEST) {
      runMigration();
    } else {
      log.info("Skipping DB migrations in TEST mode.");
    }
  }

  private void runMigration() {
    String jdbcUrl = url.replace("vertx-reactive", "jdbc");
    log.info("Migrating DB " + jdbcUrl);
    Liquibase liquibase = null;

    try {
      var resourceAccessor = new ClassLoaderResourceAccessor(Thread.currentThread().getContextClassLoader());
      var conn = DatabaseFactory.getInstance()
          .openConnection(jdbcUrl, username, password, null, resourceAccessor);

      liquibase = new Liquibase(changeLogLocation, resourceAccessor, conn);
      liquibase.update(new Contexts(), new LabelExpression());
    } catch (Exception e) {
      log.error("Liquibase Migration Exception: " + ExceptionUtil.generateStackTrace(e));
    } finally {
      if (liquibase != null) {
        try {
          liquibase.close();
        } catch (LiquibaseException e) {
          log.info(e.getMessage());
        }
      }
    }
  }

}
