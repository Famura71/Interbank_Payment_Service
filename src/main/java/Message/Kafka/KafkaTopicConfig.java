package Message.Kafka;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

    // Kafka server adress
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    // Kafka Admin
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        return new KafkaAdmin(configs);
    }

    //Payment-transactions
    @Bean
    public NewTopic paymentTransactionsTopic() {
        return new NewTopic(
            "payment-transactions",  // Topic name
            1,              // Partition number
            (short) 1                      // Replication factor
        );
    }
}