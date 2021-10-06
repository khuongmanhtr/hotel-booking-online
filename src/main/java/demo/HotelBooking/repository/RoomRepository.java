package demo.HotelBooking.repository;

import demo.HotelBooking.entity.Room;
import demo.HotelBooking.helper.IRoomCustom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoomRepository extends CrudRepository<Room, Integer> {
    Room findByName(String name);
    Room findById(int id);

    @Query(value = "select t1.room_name as roomName, rt.room_type_name as typeName, t1.status as currentStatus from \n" +
            "\t(select * from \n" +
            "\t\t(\n" +
            "\t\t\tselect * , \"diff type\" as status from room r \n" +
            "\t\t\twhere r.is_active = 1\n" +
            "\t\t\tand r.room_type_id not in \n" +
            "\t\t\t\t(select rt.room_type_id from booking_code bc \n" +
            "\t\t\t\t\tjoin booking_detail using (booking_code)\n" +
            "\t\t\t\t\tjoin room_type rt using (room_type_id)\n" +
            "\t\t\t\t\twhere bc.booking_code = ?1 \n" +
            "\t\t\t\t)\n" +
            "\t\tunion\n" +
            "\t\t\tselect *, \"same type\" as status from room r \n" +
            "\t\t\twhere r.is_active = 1\n" +
            "\t\t\tand r.room_type_id in \n" +
            "\t\t\t\t(select rt.room_type_id from booking_code bc \n" +
            "\t\t\t\t\tjoin booking_detail using (booking_code)\n" +
            "\t\t\t\t\tjoin room_type rt using (room_type_id)\n" +
            "\t\t\t\t\twhere bc.booking_code = ?1 \n" +
            "\t\t\t\t)\n" +
            "\t\t) t\n" +
            "\twhere t.room_name not in \n" +
            "\t\t(select r.room_name from room r \n" +
            "\t\twhere r.room_name in \n" +
            "\t\t\t(select r.room_name from booking_code bc \n" +
            "\t\t\t\tjoin check_in_detail using (booking_code)\n" +
            "\t\t\t\tjoin room r using (room_id)\n" +
            "\t\t\t\twhere check_in_date is NOT NULL AND check_out_date is NULL\n" +
            "\t\t\t)\n" +
            "\t\t)\n" +
            "\tunion\n" +
            "\t\tselect *, \"being used\" as status from room r \n" +
            "\t\twhere r.room_name in \n" +
            "\t\t\t(select r.room_name from booking_code bc \n" +
            "\t\t\tjoin check_in_detail using (booking_code)\n" +
            "\t\t\tjoin room r using (room_id)\n" +
            "\t\t\twhere check_in_date is NOT NULL AND check_out_date is NULL)\n" +
            "\tunion\n" +
            "\t\tselect *, \"not available\" as status from room r\n" +
            "\t\twhere r.is_active = 0\n" +
            "\t) t1\n" +
            "join room_type rt using (room_type_id)\n" +
            "order by t1.room_name", nativeQuery = true)
    List<IRoomCustom> getAllRoomInHotelWithStatusBaseOnCode(String bookingCode);

    @Query(value = "select t.room_name as roomName, rt.room_type_name as typeName, t.status as currentStatus from \n" +
            "\t(\n" +
            "\t\tselect *, \"being used\" as status from room r \n" +
            "\t\twhere r.is_active = 1\n" +
            "\t\tand r.room_name in \n" +
            "\t\t\t(select r.room_name from booking_code bc \n" +
            "\t\t\t\tjoin check_in_detail using (booking_code)\n" +
            "\t\t\t\tjoin room r using (room_id)\n" +
            "\t\t\t\twhere check_in_date is NOT NULL AND check_out_date is NULL)\n" +
            "\tunion\n" +
            "\t\tselect *, \"available\" as status from room r \n" +
            "\t\twhere r.is_active = 1\n" +
            "\t\tand r.room_name not in \n" +
            "\t\t\t(select r.room_name from booking_code bc \n" +
            "\t\t\t\tjoin check_in_detail using (booking_code)\n" +
            "\t\t\t\tjoin room r using (room_id)\n" +
            "\t\t\t\twhere check_in_date is NOT NULL AND check_out_date is NULL)\n" +
            "\tunion\n" +
            "\t\tselect *, \"not available\" as status from room r\n" +
            "\t\twhere r.is_active = 0\n" +
            "\t) t\n" +
            "join room_type rt using (room_type_id)\n" +
            "order by t.room_name asc;" , nativeQuery = true)
    List<IRoomCustom> getAllRoomInHotelWithStatus();
}
