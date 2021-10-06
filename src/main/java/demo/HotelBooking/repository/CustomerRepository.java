package demo.HotelBooking.repository;

import demo.HotelBooking.entity.Customer;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    @Query(value = "select count(c) from Customer c")
    int countAllCustomer();

    @Query(value = "select c from Customer c order by c.bookingCodeDetail.dateFrom")
    List<Customer>getAllCustomer(Pageable pageable);

    @Query(value = "select count(1) from customer " +
            "where customer_name regexp ?", nativeQuery = true)
    int countAllCustomerByName(String searchValue);

    @Query(value = "select * from customer " +
            "where customer_name regexp ?1 ", nativeQuery = true)
    List<Customer>getAllCustomerByName(String searchValue, Pageable pageable);

    @Query(value = "select count(1) from customer " +
            "where booking_code regexp ?1 ", nativeQuery = true)
    int countAllCustomerByCode(String searchValue);

    @Query(value = "select * from customer " +
            "where booking_code regexp ?1 ", nativeQuery = true)
    List<Customer>getAllCustomerByCode(String searchValue, Pageable pageable);

    @Query(value = "select count(1) from customer c " +
            "join room_promotion_code rpd " +
            "on c.room_promotion_id = rpd.id " +
            "where rpd.room_promotion_code regexp ?1 ", nativeQuery = true)
    int countAllCustomerByRoomPromo(String searchValue);

    @Query(value = "select * from customer c " +
            "join room_promotion_code rpd " +
            "on c.room_promotion_id = rpd.id " +
            "where rpd.room_promotion_code regexp ?1 ", nativeQuery = true)
    List<Customer>getAllCustomerByRoomPromo(String searchValue, Pageable pageable);

    @Query(value = "select count(1) from customer c " +
            "join service_promotion_code spd  " +
            "on c.service_promotion_id = spd.id " +
            "where spd.service_promotion_code regexp ?1 ", nativeQuery = true)
    int countAllCustomerByServicePromo(String searchValue);

    @Query(value = "select * from customer c " +
            "join service_promotion_code spd  " +
            "on c.service_promotion_id = spd.id " +
            "where spd.service_promotion_code regexp ?1 ", nativeQuery = true)
    List<Customer>getAllCustomerByServicePromo(String searchValue, Pageable pageable);

    @Query(value = "select count(1) from customer " +
            "where bank_account regexp ?1 ", nativeQuery = true)
    int countAllCustomerByBankAccount(String searchValue);

    @Query(value = "select * from customer " +
            "where bank_account regexp ?1 ", nativeQuery = true)
    List<Customer>getAllCustomerByBankAccount(String searchValue, Pageable pageable);

}
