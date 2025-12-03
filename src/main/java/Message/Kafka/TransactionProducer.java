package Message.Kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionProducer {

    // Take KafkaTemplate from Spring
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    // Topic name
    private static final String TOPIC = "payment-transactions";

    /**
     * Send the Transaction to Kafka
     * 
     * @param transactionId Transaction ID (key)
     * @param transactionData Transaction verisi (JSON string)
     */
    public void sendTransaction(String transactionId, String transactionData) {
        try {
            // Send to Kafka
            kafkaTemplate.send(TOPIC, transactionId, transactionData);
            
            // Log
            System.out.println("Transaction sent to Kafka:");
            System.out.println("  Topic: " + TOPIC);
            System.out.println("  Key: " + transactionId);
            System.out.println("  Data: " + transactionData);
            
        } catch (Exception e) {
            // Hata yakalama
            System.err.println("Failed to send transaction to Kafka: " + e.getMessage());
            e.printStackTrace();
        }
    }
}