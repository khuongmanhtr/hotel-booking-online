package demo.HotelBooking.repository;

import demo.HotelBooking.entity.HotelBankAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelBankAccountRepository extends CrudRepository<HotelBankAccount, Integer> {
    HotelBankAccount findById (int id);
}
