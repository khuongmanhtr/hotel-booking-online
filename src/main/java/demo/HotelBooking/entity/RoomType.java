package demo.HotelBooking.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "room_type")
public class RoomType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_type_id")
    private int id;

    @Column(name = "room_type_name", unique = true)
    private String name;

    @Column(name = "price")
    private double price;

    @Column(name = "number_of_people")
    private int numberOfPeople;

    @Column(name = "description")
    private String description;

    @Column(name = "avatar")
    private String avatar;

    @OneToMany(mappedBy = "roomType")
    private List<Room> roomList;

    @OneToMany(mappedBy = "roomType")
    private List<RoomTypeImage> roomTypeImageList;

    @OneToMany(mappedBy = "roomType")
    private List<BookingDetail> bookingDetailList;

    public RoomType() {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(List<Room> roomList) {
        this.roomList = roomList;
    }

    public List<RoomTypeImage> getRoomTypeImageList() {
        return roomTypeImageList;
    }

    public void setRoomTypeImageList(List<RoomTypeImage> roomTypeImageList) {
        this.roomTypeImageList = roomTypeImageList;
    }

    public List<BookingDetail> getBookingDetailList() {
        return bookingDetailList;
    }

    public void setBookingDetailList(List<BookingDetail> bookingDetailList) {
        this.bookingDetailList = bookingDetailList;
    }
}
