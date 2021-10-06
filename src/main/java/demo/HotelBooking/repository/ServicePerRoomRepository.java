package demo.HotelBooking.repository;

import demo.HotelBooking.entity.ServicePerRoom;
import demo.HotelBooking.helper.IServiceCustom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicePerRoomRepository extends CrudRepository<ServicePerRoom, Integer> {

    @Query(value = "select t.service_name as serviceName, t.price as price, ifnull(discount_percent, 0) as discount, t.totalQuantity as totalQuantity  from " +
            "(select slpr.booking_code, s.service_id, s.service_name, s.price, count(service_name) as totalQuantity  " +
            "from service_list_per_room slpr " +
            "join service s using (service_id) " +
            "where booking_code = ?1  " +
            "group by service_name " +
            "    ) t " +
            "left join " +
            "(select spd.service_id, spc.service_promotion_code, spd.discount_percent from service_promotion_detail spd " +
            "join service_promotion_code spc on spd.service_promotion_code_id = spc.id " +
            "WHERE spc.service_promotion_code = ?2 " +
            "    ) t1 " +
            "on t.service_id = t1.service_id" , nativeQuery = true)
    List<IServiceCustom> getTotalServiceWithDiscount (String code, String servicePromoCode);
}
