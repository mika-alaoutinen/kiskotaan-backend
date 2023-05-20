package mikaa.players.testcontainers;

import org.testcontainers.utility.DockerImageName;

public interface Images {

  public DockerImageName apicurio = DockerImageName.parse("apicurio/apicurio-registry-mem:latest-release");
  public DockerImageName kafka = DockerImageName.parse("confluentinc/cp-kafka");

}
