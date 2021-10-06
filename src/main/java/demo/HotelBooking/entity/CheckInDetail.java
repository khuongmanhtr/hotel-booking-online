package demo.HotelBooking.entity;

import javax.persistence.*;

@Entity
@Table(name = "check_in_detail")
public class CheckInDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "actual_number_of_people")
    private int actualPeople;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "booking_code")
    private BookingCode bookingCode;

    public CheckInDetail() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActualPeople() {
        return actualPeople;
    }

    public void setActualPeople(int actualPeople) {
        this.actualPeople = actualPeople;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public BookingCode getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(BookingCode bookingCode) {
        this.bookingCode = bookingCode;
    }
}
