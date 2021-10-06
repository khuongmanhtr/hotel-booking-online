package demo.HotelBooking.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "room_type_image")
public class RoomTypeImage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "image")
    private String image;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomType roomType;

    public RoomTypeImage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
}
