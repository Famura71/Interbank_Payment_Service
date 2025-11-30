package Database.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Bank")
public class Bank {
    
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String bankName;

    @Column(nullable = false)
    private int cut;

    //empty constructor to not get errors
    public Bank() {}

    //Getters-Setters

    public Long getId(){
        return id;
    }
    public String getBankName(){
        return bankName;
    }
    public void setBankName(String bankName){
        this.bankName = bankName;
    }
    public int getCut(){
        return cut;
    }
    public void setCut(int cut){
        this.cut = cut;
    }


}
