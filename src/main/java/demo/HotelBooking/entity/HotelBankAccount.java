package demo.HotelBooking.entity;

import javax.persistence.*;

@Entity
@Table(name = "hotel_bank_account")
public class HotelBankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "bank_account")
    private String bankAccount;

    public HotelBankAccount() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }
}
