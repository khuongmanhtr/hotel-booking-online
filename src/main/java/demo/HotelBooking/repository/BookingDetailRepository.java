package demo.HotelBooking.repository;

import demo.HotelBooking.entity.BookingDetail;
import demo.HotelBooking.entity.RoomType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface BookingDetailRepository extends CrudRepository<BookingDetail, Integer> {
    @Query("select bd.roomType.name from BookingDetail bd where bd.bookingCode.bookingDate < ?1")
    List<String> getBookingDetailWithDateLessThan(Date date);

    @Query("select bd from BookingDetail bd where bd.bookingCode.bookingCode = ?1 and bd.roomType.name = ?2")
    BookingDetail getBookingDetailByBookingCodeAndRoomType(String code, String roomType);

    @Query("select count(bd) from BookingDetail bd where bd.status.id = 2 ")
    int countAllCustomerRequest();

    @Query("select bd from BookingDetail bd where bd.status.id = 2 order by bd.bookingCode.dateFrom desc")
    List<BookingDetail> getAllCustomerRequest(Pageable pageable);
}
