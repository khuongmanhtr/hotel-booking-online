package demo.HotelBooking.entity;

import javax.persistence.*;

@Entity
@Table(name = "service_promotion_detail")
public class ServicePromotionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "discount_percent")
    private double discountPercent;

    @ManyToOne
    @JoinColumn(name = "service_promotion_code_id")
    private ServicePromotionCode servicePromotionCode;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity serviceEntity;

    public ServicePromotionDetail() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(double discountPercent) {
        this.discountPercent = discountPercent;
    }

    public ServicePromotionCode getServicePromotionCode() {
        return servicePromotionCode;
    }

    public void setServicePromotionCode(ServicePromotionCode servicePromotionCode) {
        this.servicePromotionCode = servicePromotionCode;
    }

    public ServiceEntity getServiceEntity() {
        return serviceEntity;
    }

    public void setServiceEntity(ServiceEntity serviceEntity) {
        this.serviceEntity = serviceEntity;
    }
}
