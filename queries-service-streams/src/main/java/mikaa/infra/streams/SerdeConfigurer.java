package mikaa.infra.streams;

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

  @ConfigProperty(name = "mp.messaging.connector.smallrye-kafka.apicurio.registry.url", defaultValue = "empty")
  private String apicurioUrl;

  <T extends SpecificRecord> Serde<T> specificSerde(Class<T> type) {
    Map<String, Object> config = new HashMap<>();
    config.put(AvroKafkaSerdeConfig.AVRO_ENCODING, AvroKafkaSerdeConfig.AVRO_ENCODING_JSON);
    config.put(AvroKafkaSerdeConfig.USE_SPECIFIC_AVRO_READER, true);
    config.put(SerdeConfig.AUTO_REGISTER_ARTIFACT, true);
    config.put(SerdeConfig.REGISTRY_URL, apicurioUrl);

    var serde = new AvroSerde<T>();
    serde.configure(config, false);

    return serde;
  }

}
