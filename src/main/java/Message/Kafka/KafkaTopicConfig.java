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

    // Kafka sunucu adresi
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";

    // Kafka Admin (Topic yönetimi için)
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        return new KafkaAdmin(configs);
    }

    // Topic tanımı: payment-transactions
    @Bean
    public NewTopic paymentTransactionsTopic() {
        return new NewTopic(
            "payment-transactions",  // Topic adı
            1,                        // Partition sayısı, topic kaça bölünsün, pararlel işleme için her partitionu
            (short) 1                 // Replication factor
        );
    }
}