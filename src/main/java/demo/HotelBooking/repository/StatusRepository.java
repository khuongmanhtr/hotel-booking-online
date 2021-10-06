package demo.HotelBooking.repository;

import demo.HotelBooking.entity.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends CrudRepository<Status, Integer> {
    Status findById (int id);
}
