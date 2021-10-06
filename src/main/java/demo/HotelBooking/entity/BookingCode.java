package demo.HotelBooking.entity;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "booking_code")
public class BookingCode {
    @Id
    @Column(name = "booking_code")
    private String bookingCode;

    @Column(name = "booking_date")
    private Date bookingDate;

    @Column(name = "date_from")
    private Date dateFrom;

    @Column(name = "date_to")
    private Date dateTo;

    @Column(name = "check_in_date")
    private Date checkInDate;

    @Column(name = "check_out_date")
    private Date checkOutDate;

    @OneToMany(mappedBy = "bookingCode")
    private List<BookingDetail> bookingDetailList;

    @OneToMany(mappedBy = "bookingCode")
    private List<CheckInDetail> checkInDetailList;

    @OneToOne(mappedBy = "bookingCodeDetail")
    private Customer customer;

    @OneToMany(mappedBy = "bookingCode")
    private List<ServicePerRoom> servicePerRoomList;

    public BookingCode() {
    }

    public String getBookingCode() {
        return bookingCode;
    }

    public void setBookingCode(String bookingCode) {
        this.bookingCode = bookingCode;
    }

    public Date getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Date bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public List<BookingDetail> getBookingDetailList() {
        return bookingDetailList;
    }

    public void setBookingDetailList(List<BookingDetail> bookingDetailList) {
        this.bookingDetailList = bookingDetailList;
    }

    public List<CheckInDetail> getCheckInDetailList() {
        return checkInDetailList;
    }

    public void setCheckInDetailList(List<CheckInDetail> checkInDetailList) {
        this.checkInDetailList = checkInDetailList;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<ServicePerRoom> getServicePerRoomList() {
        return servicePerRoomList;
    }

    public void setServicePerRoomList(List<ServicePerRoom> servicePerRoomList) {
        this.servicePerRoomList = servicePerRoomList;
    }
}
