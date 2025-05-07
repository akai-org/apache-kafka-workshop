package pl.org.akai.kafkaintro.config;

import io.confluent.kafka.serializers.protobuf.KafkaProtobufDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import pl.org.akai.kafkaintro.model.KafkaMessage.ItemToProcessMessage;
import pl.org.akai.kafkaintro.model.KafkaMessage.ItemToSaveMessage;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootStrapServerAddress;

    @Value("${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryServerAddress;

    @Bean
    public ConsumerFactory<String, ItemToProcessMessage> itemToProcessMessageConsumerFactory() {
        Map<String, Object> props = new HashMap<>(baseProps());
        props.put("specific.protobuf.value.type", ItemToProcessMessage.class.getName());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ItemToProcessMessage> itemToProcessKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, ItemToProcessMessage>();
        factory.setConsumerFactory(itemToProcessMessageConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ItemToSaveMessage> itemToSaveMessageConsumerFactory() {
        Map<String, Object> props = new HashMap<>(baseProps());
        props.put("specific.protobuf.value.type", ItemToSaveMessage.class.getName());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ItemToSaveMessage> itemToSaveMessageKafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, ItemToSaveMessage>();
        factory.setConsumerFactory(itemToSaveMessageConsumerFactory());
        return factory;
    }

    private Map<String, Object> baseProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServerAddress);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaProtobufDeserializer.class);
        props.put("schema.registry.url", schemaRegistryServerAddress);
        return props;
    }
}
