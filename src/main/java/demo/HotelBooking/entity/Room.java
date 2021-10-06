package demo.HotelBooking.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private int id;

    @Column(name = "room_name")
    private String name;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "room")
    private List<ServicePerRoom> servicePerRoomList;

    @OneToMany(mappedBy = "room")
    private List<CheckInDetail> checkInDetailList;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    public Room() {
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public List<ServicePerRoom> getServicePerRoomList() {
        return servicePerRoomList;
    }

    public void setServicePerRoomList(List<ServicePerRoom> servicePerRoomList) {
        this.servicePerRoomList = servicePerRoomList;
    }

    public List<CheckInDetail> getCheckInDetailList() {
        return checkInDetailList;
    }

    public void setCheckInDetailList(List<CheckInDetail> checkInDetailList) {
        this.checkInDetailList = checkInDetailList;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

}
