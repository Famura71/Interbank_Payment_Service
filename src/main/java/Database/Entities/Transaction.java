package Database.Entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sending Account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sending_account_id", nullable = false)
    private Account sendingAccount;

    // Recieving Account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiving_account_id", nullable = false)
    private Account receivingAccount;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

    //empty constructor to not get errors
    public Transaction() {}

    //Getetrs-Setters
    
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public Account getSendingAccount(){
        return sendingAccount;
    }
    public void setSendingAccount(Account sendingAccount){
        this.sendingAccount = sendingAccount;
    }

    public Account getReceivingAccount(){
        return receivingAccount;
    }
    public void setReceivingAccount(Account receivingAccount){
        this.receivingAccount = receivingAccount;
    }

    public double getAmount(){
        return amount;
    }
    public void setAmount(double amount){
        this.amount = amount;
    }

    public LocalDateTime getTimestamp(){
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp){
        this.timestamp = timestamp;
    }
}
