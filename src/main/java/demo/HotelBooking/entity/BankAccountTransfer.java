package demo.HotelBooking.entity;

public class BankAccountTransfer {
    private String account1;
    private String account2;
    private double amount;
    private String description;

    public BankAccountTransfer(String account1, String account2, double amount, String description) {
        this.account1 = account1;
        this.account2 = account2;
        this.amount = amount;
        this.description = description;
    }

    public String getAccount1() {
        return account1;
    }

    public void setAccount1(String account1) {
        this.account1 = account1;
    }

    public String getAccount2() {
        return account2;
    }

    public void setAccount2(String account2) {
        this.account2 = account2;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
