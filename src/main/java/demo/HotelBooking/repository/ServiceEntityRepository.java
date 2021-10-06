package demo.HotelBooking.repository;

import demo.HotelBooking.entity.ServiceEntity;
import demo.HotelBooking.helper.IServiceEntityCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceEntityRepository extends CrudRepository<ServiceEntity, Integer> {
    ServiceEntity findByName(String name);
    ServiceEntity findById(int id);

    @Query("select count(se) from ServiceEntity se")
    int countAllService();

    @Query(value = "select t.service_id as serviceId, t.service_name as serviceName,  " +
            " t.description as description, t.price as price, t.status as currentStatus " +
            "from ( " +
            " select *, 'uneditable' as status from service " +
            " where service_name in  " +
            "  (select s.service_name from service s " +
            "   join service_list_per_room using (service_id) " +
            "   join booking_code bc using (booking_code) " +
            "   where bc.check_in_date is not null and bc.check_out_date is null " +
            "  ) " +
            " union " +
            "  select *, 'editable' as status from service " +
            "  where service_name not in  " +
            "   (select s.service_name from service s " +
            "    join service_list_per_room using (service_id) " +
            "    join booking_code bc using (booking_code) " +
            "    where bc.check_in_date is not null and bc.check_out_date is null " +
            "   ) " +
            " ) t " +
            "order by t.service_id", nativeQuery = true)
    List<IServiceEntityCustom> getAllService(Pageable pageable);

    @Query(value = "select count(1) from service where service_name regexp ?1 ", nativeQuery = true)
    int countAllServiceByName(String searchValue);

    @Query(value = "select t.service_id as serviceId, t.service_name as serviceName,  " +
            " t.description as description, t.price as price, t.status as currentStatus " +
            "from ( " +
            " select *, 'uneditable' as status from service " +
            " where service_name in  " +
            "  (select s.service_name from service s " +
            "   join service_list_per_room using (service_id) " +
            "   join booking_code bc using (booking_code) " +
            "   where bc.check_in_date is not null and bc.check_out_date is null " +
            "  ) " +
            " union " +
            "  select *, 'editable' as status from service " +
            "  where service_name not in  " +
            "   (select s.service_name from service s " +
            "    join service_list_per_room using (service_id) " +
            "    join booking_code bc using (booking_code) " +
            "    where bc.check_in_date is not null and bc.check_out_date is null " +
            "   ) " +
            " ) t " +
            "where t.service_name regexp ?1", nativeQuery = true)
    List<IServiceEntityCustom> getAllServiceByName(String searchValue, Pageable pageable);

}
