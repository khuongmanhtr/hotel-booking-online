package demo.HotelBooking.repository;

import demo.HotelBooking.entity.CheckInDetail;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckInDetailRepository extends CrudRepository<CheckInDetail, Integer> {
    @Query("select cid from CheckInDetail cid " +
            "where cid.bookingCode.checkInDate is not null " +
            "and cid.bookingCode.checkOutDate is null " +
            "order by cid.room.name asc")
    List<CheckInDetail> getAllActiveRooms (Pageable pageable);

    @Query("select count(cid) from CheckInDetail cid " +
            "where cid.bookingCode.checkInDate is not null " +
            "and cid.bookingCode.checkOutDate is null " +
            "order by cid.room.name asc")
    int countAllActiveRooms();

    @Query(value = "select * from check_in_detail " +
                "join booking_code bc using (booking_code) " +
                "join room r using (room_id) " +
                "where bc.check_in_date is not null  " +
                "and bc.check_out_date is null " +
                "and r.room_name regexp ?1 ", nativeQuery = true)
    List<CheckInDetail> getAllActiveRoomsByRoom(String room, Pageable pageable);

    @Query(value = "select count(1) from check_in_detail " +
                "join booking_code bc using (booking_code) " +
                "join room r using (room_id) " +
                "where bc.check_in_date is not null  " +
                "and bc.check_out_date is null " +
                "and r.room_name regexp ?1 ", nativeQuery = true)
    int countAllActiveRoomsByRoom(String room);

    @Query(value = "select * from check_in_detail " +
                "join booking_code bc using (booking_code) " +
                "where bc.check_in_date is not null  " +
                "and bc.check_out_date is null " +
                "and bc.booking_code regexp ?1 ", nativeQuery = true)
    List<CheckInDetail> getAllActiveRoomsByBookingCode(String bookingCode, Pageable pageable);

    @Query(value = "select count(1) from check_in_detail " +
                "join booking_code bc using (booking_code) " +
                "where bc.check_in_date is not null " +
                "and bc.check_out_date is null " +
                "and bc.booking_code regexp ?1 ", nativeQuery = true)
    int countAllActiveRoomsByBookingCode(String bookingCode);

    @Query("select cid from CheckInDetail cid " +
            "where cid.room.name = ?1 and cid.bookingCode.bookingCode = ?2")
    CheckInDetail getRoomByRoomAndCode(String room, String bookingCode);


}
