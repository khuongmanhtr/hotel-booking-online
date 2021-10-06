package demo.HotelBooking.entity;

import javax.persistence.*;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @Column(name = "booking_code")
    private String bookingCode;

    @Column(name = "customer_name")
    private String name;

    @Column(name = "bank_account")
    private String bankAccount;

    @Column(name = "advance_payment")
    private double advancePayment;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private BookingCode bookingCodeDetail;

    @ManyToOne
    @JoinColumn(name = "room_promotion_id")
    private RoomPromotionCode roomPromotionCode;

    @ManyToOne
    @JoinColumn(name = "service_promotion_id")
    private ServicePromotionCode servicePromotionCode;

    public Customer() {
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public double getAdvancePayment() {
        return advancePayment;
    }

    public void setAdvancePayment(double advancePayment) {
        this.advancePayment = advancePayment;
    }

    public BookingCode getBookingCodeDetail() {
        return bookingCodeDetail;
    }

    public void setBookingCodeDetail(BookingCode bookingCodeDetail) {
        this.bookingCodeDetail = bookingCodeDetail;
    }

    public RoomPromotionCode getRoomPromotionCode() {
        return roomPromotionCode;
    }

    public void setRoomPromotionCode(RoomPromotionCode roomPromotionCode) {
        this.roomPromotionCode = roomPromotionCode;
    }

    public ServicePromotionCode getServicePromotionCode() {
        return servicePromotionCode;
    }

    public void setServicePromotionCode(ServicePromotionCode servicePromotionCode) {
        this.servicePromotionCode = servicePromotionCode;
    }
}
