package Database.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Cut")
public class Cut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // primary key

    @ManyToOne
    @JoinColumn(name = "bank1_id")  // foreign key 1
    private Bank bank1;

    @ManyToOne
    @JoinColumn(name = "bank2_id")  // foreign key 2
    private Bank bank2;

    @JoinColumn(nullable = false)
    private double cut;

    //empty constructor to not get errors
    public Cut() {}

    //Getters-Setters

    public Long getId(){
        return id;
    }
    
    public Bank getBank1(){
        return bank1;
    }
    public void setBank1(Bank bank1){
        this.bank1 = bank1;
    }
    public Bank getBank2(){
        return bank2;
    }
    public void setBank2(Bank bank2){
        this.bank2 = bank2;
    }
    public double getCut(){
        return cut;
    }
    public void setCut(double cut){
        this.cut = cut;
    }
    public void setCut(Bank bank1, Bank bank2){
        double total = bank1.getCut() + bank2.getCut();
        cut = total;
    }
}
