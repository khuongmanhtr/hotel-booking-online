package demo.HotelBooking.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "service_list_per_room")
public class ServicePerRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "registry_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date registryDate;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private ServiceEntity serviceEntity;

    @ManyToOne
    @JoinColumn(name = "booking_code")
    private BookingCode bookingCode;

    public ServicePerRoom() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getRegistryDate() {
        return registryDate;
    }

    public void setRegistryDate(Date registryDate) {
        this.registryDate = registryDate;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public ServiceEntity getServiceEntity() {
        return serviceEntity;
    }

    public void setServiceEntity(ServiceEntity serviceEntity) {
        this.serviceEntity = serviceEntity;
    }

    public BookingCode getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(BookingCode bookingCode) {
        this.bookingCode = bookingCode;
    }
}
