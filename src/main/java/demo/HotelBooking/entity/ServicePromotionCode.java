package demo.HotelBooking.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "service_promotion_code")
public class ServicePromotionCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "service_promotion_code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "is_used")
    private boolean isUsed;

    @OneToMany(mappedBy = "servicePromotionCode")
    private List<Customer> customerList;

    @OneToMany(mappedBy = "servicePromotionCode")
    private List<ServicePromotionDetail> servicePromotionDetailList;

    public ServicePromotionCode() {
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

    public List<ServicePromotionDetail> getServicePromotionDetailList() {
        return servicePromotionDetailList;
    }

    public void setServicePromotionDetailList(List<ServicePromotionDetail> servicePromotionDetailList) {
        this.servicePromotionDetailList = servicePromotionDetailList;
    }
}
