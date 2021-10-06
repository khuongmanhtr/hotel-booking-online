package demo.HotelBooking.repository;

import demo.HotelBooking.entity.BookingCode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface BookingCodeRepository extends CrudRepository<BookingCode,String> {
    BookingCode findByBookingCode(String bookingCode);

    @Query(value = "select max(date_to) from booking_code", nativeQuery = true)
    Date getMaxDateTo();

    // All Booking Code
    @Query("select count(bc) from BookingCode bc order by bc.dateFrom desc")
    int countAllBookingCode();

    @Query("select bc from BookingCode bc order by bc.dateFrom desc")
    List<BookingCode> getAllBookingCode(Pageable pageable);

    @Query(value= "select count(1) from booking_code " +
                "where booking_code regexp ?1", nativeQuery = true)
    int countAllBookingCodeByCode(String code);

    @Query(value= "select * from booking_code " +
                  "where booking_code regexp ?1", nativeQuery = true)
    List<BookingCode>getAllBookingCodeByCode(String code, Pageable pageable);

    @Query(value = "select count(1) from booking_code " +
            "join customer cs using (booking_code) " +
            "where customer_name regexp ?1 ", nativeQuery = true)
    int countAllBookingCodeByCustomer(String customer);

    @Query(value = "select * from booking_code " +
                   "join customer cs using (booking_code) " +
                   "where customer_name regexp ?1 " +
                   "order by date_from desc", nativeQuery = true)
    List<BookingCode>getAllBookingCodeByCustomer(String customer, Pageable pageable);

    @Query("select count(bc) from BookingCode bc where bc.dateFrom = ?1")
    int countAllBookingCodeByBookFrom(Date bookFrom);

    @Query("select bc from BookingCode bc where bc.dateFrom = ?1")
    List<BookingCode>getAllBookingCodeByBookFrom(Date bookFrom, Pageable pageable);

    @Query("select count(bc) from BookingCode bc where bc.dateTo = ?1")
    int countAllBookingCodeByBookTo(Date bookTo);

    @Query("select bc from BookingCode bc where bc.dateTo = ?1 order by bc.customer.name asc")
    List<BookingCode>getAllBookingCodeByBookTo(Date bookTo, Pageable pageable);

    // All Booking Code Not Check In
    @Query(value = "select count(1) from booking_code " +
            "where booking_code in " +
            "(select bc.booking_code from booking_code bc " +
            "join booking_detail bd using (booking_code) " +
            "where check_in_date is null and check_out_date is null " +
            "and bd.status_id in (1,2,4) " +
            "group by bc.booking_code)",nativeQuery = true)
    int countAllBookingCodeNotCheckIn();

    @Query(value = "select * from booking_code " +
            "where booking_code in " +
            "(select bc.booking_code from booking_code bc " +
            "join booking_detail bd using (booking_code) " +
            "where check_in_date is null and check_out_date is null " +
            "and bd.status_id in (1,2,4) " +
            "group by bc.booking_code) " +
            "order by date_from asc", nativeQuery = true)
    List<BookingCode> getAllBookingCodeNotCheckIn(Pageable pageable); // => oke

    @Query(value = "select count(1) from booking_code " +
            "where booking_code in " +
            "(select bc.booking_code from booking_code bc " +
            "join booking_detail bd using (booking_code) " +
            "where check_in_date is null and check_out_date is null " +
            "and bd.status_id in (1,2,4) " +
            "group by bc.booking_code) " +
            "and booking_code regexp ?1 ", nativeQuery = true)
    int countAllBookingCodeNotCheckInByCode(String searchValue);

    @Query(value = "select * from booking_code " +
            "where booking_code in " +
            "(select bc.booking_code from booking_code bc " +
            "join booking_detail bd using (booking_code) " +
            "where check_in_date is null and check_out_date is null " +
            "and bd.status_id in (1,2,4) " +
            "group by bc.booking_code) " +
            "and booking_code regexp ?1", nativeQuery = true)
    List<BookingCode> getAllBookingCodeNotCheckInByCode(String searchValue, Pageable pageable);

    @Query(value = "select count(1) from booking_code " +
            "join customer using (booking_code) " +
            "where booking_code in " +
            "(select bc.booking_code from booking_code bc " +
            "join booking_detail bd using (booking_code) " +
            "where check_in_date is null and check_out_date is null " +
            "and bd.status_id in (1,2,4) " +
            "group by bc.booking_code) " +
            "and customer_name regexp ?1 ", nativeQuery = true)
    int countAllBookingCodeNotCheckInByCustomer(String searchValue);

    @Query(value = "select * from booking_code " +
            "join customer using (booking_code) " +
            "where booking_code in " +
            "(select bc.booking_code from booking_code bc " +
            "join booking_detail bd using (booking_code) " +
            "where check_in_date is null and check_out_date is null " +
            "and bd.status_id in (1,2,4) " +
            "group by bc.booking_code) " +
            "and customer_name regexp ?1 " +
            "order by date_from asc", nativeQuery = true)
    List<BookingCode> getAllBookingCodeNotCheckInByCustomer(String searchValue, Pageable pageable);

    @Query(value = "select count(1) from booking_code " +
            "where booking_code in " +
            "(select bc.booking_code from booking_code bc " +
            "join booking_detail bd using (booking_code) " +
            "where check_in_date is null and check_out_date is null " +
            "and bd.status_id in (1,2,4) " +
            "group by bc.booking_code) " +
            "and booking_date = ?1", nativeQuery = true)
    int countAllBookingCodeNotCheckInByBookDate(Date bookDate);

    @Query(value = "select * from booking_code " +
            "where booking_code in " +
            "(select bc.booking_code from booking_code bc " +
            "join booking_detail bd using (booking_code) " +
            "where check_in_date is null and check_out_date is null " +
            "and bd.status_id in (1,2,4) " +
            "group by bc.booking_code) " +
            "and booking_date = ?1", nativeQuery = true)
    List<BookingCode> getAllBookingCodeNotCheckInByBookDate(Date bookDate, Pageable pageable);

    @Query(value = "select count(1) from booking_code " +
            "where booking_code in " +
            "(select bc.booking_code from booking_code bc " +
            "join booking_detail bd using (booking_code) " +
            "where check_in_date is null and check_out_date is null " +
            "and bd.status_id in (1,2,4) " +
            "group by bc.booking_code) " +
            "and date_from = ?1", nativeQuery = true)
    int countAllBookingCodeNotCheckInByBookFrom(Date bookFromDate);

    @Query(value = "select * from booking_code " +
            "where booking_code in " +
            "(select bc.booking_code from booking_code bc " +
            "join booking_detail bd using (booking_code) " +
            "where check_in_date is null and check_out_date is null " +
            "and bd.status_id in (1,2,4) " +
            "group by bc.booking_code) " +
            "and date_from = ?1", nativeQuery = true)
    List<BookingCode> getAllBookingCodeNotCheckInByBookFrom(Date bookFromDate, Pageable pageable);

    @Query(value = "select count(1) from booking_code " +
            "where booking_code in " +
            "(select bc.booking_code from booking_code bc " +
            "join booking_detail bd using (booking_code) " +
            "where check_in_date is null and check_out_date is null " +
            "and bd.status_id in (1,2,4) " +
            "group by bc.booking_code) " +
            "and date_to = ?1", nativeQuery = true)
    int countAllBookingCodeNotCheckInByBookTo(Date bookToDate);

    @Query(value = "select * from booking_code " +
            "where booking_code in " +
            "(select bc.booking_code from booking_code bc " +
            "join booking_detail bd using (booking_code) " +
            "where check_in_date is null and check_out_date is null " +
            "and bd.status_id in (1,2,4) " +
            "group by bc.booking_code) " +
            "and date_to = ?1", nativeQuery = true)
    List<BookingCode> getAllBookingCodeNotCheckInByBookTo(Date bookToDate, Pageable pageable);

    // All Booking Code Check In
    @Query("select count(bc) from BookingCode bc " +
            "where bc.checkInDate is not null and bc.checkOutDate is null")
    int countAllBookingCodeCheckIn();

    @Query("select bc from BookingCode bc " +
            "where bc.checkInDate is not null and bc.checkOutDate is null " +
            "order by bc.dateFrom desc")
    List<BookingCode>getAllBookingCodeCheckIn(Pageable pageable);

    @Query(value = "select count(1) from booking_code " +
            "where check_in_date is not null " +
            "and check_out_date is null " +
            "and booking_code regexp ?1", nativeQuery = true)
    int countAllBookingCodeCheckInByCode(String searchValue);

    @Query(value = "select * from booking_code " +
            "where check_in_date is not null " +
            "and check_out_date is null " +
            "and booking_code regexp ?1", nativeQuery = true)
    List<BookingCode>getAllBookingCodeCheckInByCode(String searchValue, Pageable pageable);

    @Query(value = "select count(1) from booking_code " +
            "join customer cs using (booking_code) " +
            "where check_in_date is not null " +
            "and check_out_date is null " +
            "and customer_name regexp ?1 " +
            "order by date_from desc", nativeQuery = true)
    int countAllBookingCodeCheckInByCustomer(String searchValue);

    @Query(value = "select * from booking_code " +
            "join customer cs using (booking_code) " +
            "where check_in_date is not null " +
            "and check_out_date is null " +
            "and customer_name regexp ?1 " +
            "order by date_from desc", nativeQuery = true)
    List<BookingCode>getAllBookingCodeCheckInByCustomer(String searchValue, Pageable pageable);

    @Query("select count(bc) from BookingCode bc " +
            "where bc.checkInDate is not null " +
            "and bc.checkOutDate is null " +
            "and bc.bookingDate = ?1")
    int countAllBookingCodeCheckInByBookDate(Date bookDate);

    @Query("select bc from BookingCode bc " +
            "where bc.checkInDate is not null " +
            "and bc.checkOutDate is null " +
            "and bc.bookingDate = ?1")
    List<BookingCode>getAllBookingCodeCheckInByBookDate(Date bookDate, Pageable pageable);

    @Query("select count(bc) from BookingCode bc " +
            "where bc.checkInDate is not null " +
            "and bc.checkOutDate is null " +
            "and bc.dateFrom = ?1")
    int countAllBookingCodeCheckInByBookFrom(Date bookFromDate);

    @Query("select bc from BookingCode bc " +
            "where bc.checkInDate is not null " +
            "and bc.checkOutDate is null " +
            "and bc.dateFrom = ?1")
    List<BookingCode>getAllBookingCodeCheckInByBookFrom(Date bookFromDate, Pageable pageable);

    @Query("select count(bc) from BookingCode bc " +
            "where bc.checkInDate is not null " +
            "and bc.checkOutDate is null " +
            "and bc.dateTo = ?1")
    int countAllBookingCodeCheckInByBookTo(Date bookToDate);

    @Query("select bc from BookingCode bc " +
            "where bc.checkInDate is not null " +
            "and bc.checkOutDate is null " +
            "and bc.dateTo = ?1")
    List<BookingCode>getAllBookingCodeCheckInByBookTo(Date bookToDate, Pageable pageable);

    // Get booking code list to report
    List<BookingCode> findBookingCodeByBookingDateBetween(Date dateFrom, Date dateTo);


}
