package demo.HotelBooking.repository;

import demo.HotelBooking.entity.ServicePromotionDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ServicePromotionDetailRepository extends CrudRepository<ServicePromotionDetail, Integer> {
    @Query("select spd from ServicePromotionDetail spd where spd.serviceEntity.id = ?1")
    List<ServicePromotionDetail> getServicePromotionDetailListByServiceId (int id);

    @Query("select spd from ServicePromotionDetail spd where spd.servicePromotionCode.id = ?1")
    List<ServicePromotionDetail> getServicePromotionDetailListByPromoId (int id);
}
