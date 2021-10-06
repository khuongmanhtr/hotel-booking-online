package demo.HotelBooking.repository;

import demo.HotelBooking.entity.ServicePromotionCode;
import demo.HotelBooking.helper.IServicePromoCodeCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServicePromotionCodeRepository extends CrudRepository<ServicePromotionCode, Integer> {
    ServicePromotionCode findByCodeAndIsUsed (String code, Boolean isUsed);
    ServicePromotionCode findByCode(String code);
    ServicePromotionCode findById(int id);

    @Query(value = "select count(1) from  " +
            " ( " +
            "  select *, 'being used' as currentStatus from service_promotion_code spc " +
            "  where spc.is_used = 1 " +
            "  and spc.id in  " +
            "   (select c.service_promotion_id from booking_code bc " +
            "    join customer c using (booking_code) " +
            "    where bc.check_out_date is null " +
            "   ) " +
            " union " +
            "  select *, 'expired' as currentStatus from service_promotion_code spc " +
            "  where spc.is_used = 1 " +
            "  and spc.id in  " +
            "   (select c.service_promotion_id from booking_code bc " +
            "    join customer c using (booking_code) " +
            "    where bc.check_out_date is not null " +
            "   ) " +
            " union " +
            "  select *, 'available' as currentStatus from service_promotion_code spc " +
            "  where spc.is_used = 0 " +
            " ) t", nativeQuery = true)
    int countAllPromoCode();

    @Query(value = "select t.id as servicePromoId, t.service_promotion_code as servicePromoCode, t.description, " +
            " t.is_used as isUsed, t.currentStatus as currentStatus from  " +
            " ( " +
            "  select *, 'being used' as currentStatus from service_promotion_code spc " +
            "  where spc.is_used = 1 " +
            "  and spc.id in  " +
            "   (select c.service_promotion_id from booking_code bc " +
            "    join customer c using (booking_code) " +
            "    where bc.check_out_date is null " +
            "   ) " +
            " union " +
            "  select *, 'expired' as currentStatus from service_promotion_code spc " +
            "  where spc.is_used = 1 " +
            "  and spc.id in  " +
            "   (select c.service_promotion_id from booking_code bc " +
            "    join customer c using (booking_code) " +
            "    where bc.check_out_date is not null " +
            "   ) " +
            " union " +
            "  select *, 'available' as currentStatus from service_promotion_code spc " +
            "  where spc.is_used = 0 " +
            " ) t", nativeQuery = true)
    List<IServicePromoCodeCustom> getAllPromoCode(Pageable pageable);

    @Query(value = "select count(1) from  " +
            " ( " +
            "  select *, 'being used' as currentStatus from service_promotion_code spc " +
            "  where spc.is_used = 1 " +
            "  and spc.id in  " +
            "   (select c.service_promotion_id from booking_code bc " +
            "    join customer c using (booking_code) " +
            "    where bc.check_out_date is null " +
            "   ) " +
            " union " +
            "  select *, 'expired' as currentStatus from service_promotion_code spc " +
            "  where spc.is_used = 1 " +
            "  and spc.id in  " +
            "   (select c.service_promotion_id from booking_code bc " +
            "    join customer c using (booking_code) " +
            "    where bc.check_out_date is not null " +
            "   ) " +
            " union " +
            "  select *, 'available' as currentStatus from service_promotion_code spc " +
            "  where spc.is_used = 0 " +
            " ) t " +
            "where service_promotion_code regexp ?1", nativeQuery = true)
    int countAllPromoCodeByCode(String searchValue);

    @Query(value = "select t.id as servicePromoId, t.service_promotion_code as servicePromoCode, t.description, " +
            " t.is_used as isUsed, t.currentStatus as currentStatus from  " +
            " ( " +
            "  select *, 'being used' as currentStatus from service_promotion_code spc " +
            "  where spc.is_used = 1 " +
            "  and spc.id in  " +
            "   (select c.service_promotion_id from booking_code bc " +
            "    join customer c using (booking_code) " +
            "    where bc.check_out_date is null " +
            "   ) " +
            " union " +
            "  select *, 'expired' as currentStatus from service_promotion_code spc " +
            "  where spc.is_used = 1 " +
            "  and spc.id in  " +
            "   (select c.service_promotion_id from booking_code bc " +
            "    join customer c using (booking_code) " +
            "    where bc.check_out_date is not null " +
            "   ) " +
            " union " +
            "  select *, 'available' as currentStatus from service_promotion_code spc " +
            "  where spc.is_used = 0 " +
            " ) t " +
            "where service_promotion_code regexp ?1", nativeQuery = true)
    List<IServicePromoCodeCustom> getAllPromoCodeByCode(String searchValue, Pageable pageable);


}
