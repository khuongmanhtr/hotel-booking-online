package demo.HotelBooking.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "room_promotion_code")
public class RoomPromotionCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "room_promotion_code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "discount_percent")
    private double discountPercent;

    @Column(name = "is_used")
    private boolean isUsed;

    @OneToMany(mappedBy = "roomPromotionCode")
    private List<Customer> customerList;

    public RoomPromotionCode() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }
}
