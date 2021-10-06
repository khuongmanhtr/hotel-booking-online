package demo.HotelBooking.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "service")
public class ServiceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "service_id")
    private int id;

    @Column(name = "service_name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private double price;

    @OneToMany (mappedBy = "serviceEntity")
    private List<ServicePerRoom> servicePerRoomList;

    @OneToMany (mappedBy = "serviceEntity")
    private List<ServicePromotionDetail> servicePromotionDetailList;

    public ServiceEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<ServicePerRoom> getServicePerRoomList() {
        return servicePerRoomList;
    }

    public void setServicePerRoomList(List<ServicePerRoom> servicePerRoomList) {
        this.servicePerRoomList = servicePerRoomList;
    }

    public List<ServicePromotionDetail> getServicePromotionDetailList() {
        return servicePromotionDetailList;
    }

    public void setServicePromotionDetailList(List<ServicePromotionDetail> servicePromotionDetailList) {
        this.servicePromotionDetailList = servicePromotionDetailList;
    }
}
