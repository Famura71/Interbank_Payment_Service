package Message.Kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionProducer {

    // Spring'den KafkaTemplate iste
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Topic adı
    private static final String TOPIC = "payment-transactions";

    /**
     * Transaction'ı Kafka'ya gönder
     * 
     * @param transactionId Transaction ID (key)
     * @param transactionData Transaction verisi (JSON string)
     */
    public void sendTransaction(String transactionId, String transactionData) {
        try {
            // Kafka'ya gönder
            kafkaTemplate.send(TOPIC, transactionId, transactionData);
            
            // Log
            System.out.println("✅ Transaction sent to Kafka:");
            System.out.println("  Topic: " + TOPIC);
            System.out.println("  Key: " + transactionId);
            System.out.println("  Data: " + transactionData);
            
        } catch (Exception e) {
            // Hata yakalama
            System.err.println("❌ Failed to send transaction to Kafka: " + e.getMessage());
            e.printStackTrace();
        }
    }
}