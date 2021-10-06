package demo.HotelBooking.repository;

import demo.HotelBooking.entity.RoomType;
import demo.HotelBooking.helper.IRoomTypeCustom;
import demo.HotelBooking.helper.IRoomTypeStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface RoomTypeRepository extends CrudRepository<RoomType, Integer> {
    RoomType findByName(String name);
    RoomType findById(int id);

    @Query(value = "select ifnull(sum(quantity),0) as total_room from room_type rt " +
            "left join  " +
            "\t(select room_type_id, bc.booking_code ,quantity, date_from, date_to, bd.status_id " +
            "\t\tfrom booking_detail bd " +
            "\t\tjoin booking_code bc using (booking_code) " +
            "\t\twhere date_to > ?1 and date_from < ?2 " +
            "       and bd.status_id in (1,2,4) " +
            "\t) t1 " +
            "on rt.room_type_id = t1.room_type_id " +
            "group by room_type_name " +
            "having room_type_name = ?3", nativeQuery = true)
    long getTotalRoomPerDay (Date dateFromCheck,Date dateToCheck,String roomTypeName);

    @Query(value = "select ifnull(total,0) as total_room from room_type rt\n" +
            "left join\n" +
            "\t(select room_type_name ,count(room_type_name) as total from room_type\n" +
            "\t\tjoin room using (room_type_id)\n" +
            "\t\tgroup by (room_type_name)) t\n" +
            "on t.room_type_name = rt.room_type_name\n" +
            "where rt.room_type_name = ?1", nativeQuery = true)
    long getTotalRoomByRoomType(String roomTypeName);

    @Query(value = "select rt.room_type_name as typeName, ifnull(total,0) as parameter from room_type rt " +
            "left join " +
            " (select room_type_name ,count(room_type_name) as total from room_type " +
            "  join room using (room_type_id) " +
            "  group by (room_type_name)) t  " +
            "on t.room_type_name = rt.room_type_name " +
            "order by rt.room_type_name asc", nativeQuery = true)
    List<IRoomTypeCustom> getAllTotalRoomPerRoomType();

    @Query(value = "select rt.room_type_name as typeName, ifnull(being_used,0) as parameter from room_type rt " +
            "left join " +
            " (select r.room_type_id, count(room_type_id) as being_used from booking_code bc " +
            " join check_in_detail using (booking_code) " +
            " join room r using (room_id) " +
            " where check_in_date is NOT NULL AND check_out_date is NULL " +
            " group by r.room_type_id) t " +
            "using (room_type_id) " +
            "order by rt.room_type_name asc;", nativeQuery = true)
    List<IRoomTypeCustom> getTotalBeingUsedPerRoomType();

    @Query(value = "select count(1) " +
            "from (select *, 'uneditable' as currentStatus from room_type\n" +
            "\t\twhere room_type_id in (select r.room_type_id from room r)\n" +
            "\tunion\n" +
            "\t\tselect *, 'editable' as currentStatus from room_type\n" +
            "\t\twhere room_type_id not in (select r.room_type_id from room r)\n" +
            "\t) t", nativeQuery = true)
    int countAllRoomTypeStatus();

    @Query(value = "select t.room_type_id as roomTypeId, t.room_type_name as roomTypeName, t.number_of_people as numberOfPeople, \n" +
            "\t\tt.price as price, t.avatar as image, t.currentStatus as currentStatus\n" +
            "from (select *, 'uneditable' as currentStatus from room_type\n" +
            "\t\twhere room_type_id in (select r.room_type_id from room r)\n" +
            "\tunion\n" +
            "\t\tselect *, 'editable' as currentStatus from room_type\n" +
            "\t\twhere room_type_id not in (select r.room_type_id from room r)\n" +
            "\t) t", nativeQuery = true)
    List<IRoomTypeStatus> getAllRoomTypeStatus(Pageable pageable);

    @Query(value = "select count(1) " +
            "from (select *, 'uneditable' as currentStatus from room_type\n" +
            "\t\twhere room_type_id in (select r.room_type_id from room r)\n" +
            "\tunion\n" +
            "\t\tselect *, 'editable' as currentStatus from room_type\n" +
            "\t\twhere room_type_id not in (select r.room_type_id from room r)\n" +
            "\t) t\n" +
            "where t.room_type_name regexp ?1 ", nativeQuery = true)
    int countAllRoomTypeStatusByName(String roomTypeName);

    @Query(value = "select t.room_type_id as roomTypeId, t.room_type_name as roomTypeName, t.number_of_people as numberOfPeople, \n" +
            "\t\tt.price as price, t.avatar as image, t.currentStatus as currentStatus\n" +
            "from (select *, 'uneditable' as currentStatus from room_type\n" +
            "\t\twhere room_type_id in (select r.room_type_id from room r)\n" +
            "\tunion\n" +
            "\t\tselect *, 'editable' as currentStatus from room_type\n" +
            "\t\twhere room_type_id not in (select r.room_type_id from room r)\n" +
            "\t) t\n" +
            "where t.room_type_name regexp ?1 ", nativeQuery = true)
    List<IRoomTypeStatus> getAllRoomTypeStatusByName(String roomTypeName, Pageable pageable);
}
