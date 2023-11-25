package mikaa.streams;

import java.util.HashMap;
import java.util.Map;

import org.apache.avro.specific.SpecificRecord;
import org.apache.kafka.common.serialization.Serde;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.apicurio.registry.serde.SerdeConfig;
import io.apicurio.registry.serde.avro.AvroKafkaSerdeConfig;
import io.apicurio.registry.serde.avro.AvroSerde;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
class SerdeConfigurer {

  @ConfigProperty(name = "mp.messaging.connector.smallrye-kafka.apicurio.registry.url", defaultValue = "invalid")
  private String apicurioUrl;

  <T extends SpecificRecord> Serde<T> inputSerde(Class<T> type) {
    Map<String, Object> config = new HashMap<>();
    config.put(AvroKafkaSerdeConfig.USE_SPECIFIC_AVRO_READER, true);
    config.put(SerdeConfig.REGISTRY_URL, "http://localhost:8888/apis/registry/v2");

    var serde = new AvroSerde<T>();
    serde.configure(config, false);

    return serde;
  }

}
