package demo.HotelBooking.security;

import demo.HotelBooking.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    User findByUsername (String username);

    @Query(value = "select count(1) from user where username regexp ?1", nativeQuery = true)
    int countAllAccountByUsername(String searchValue);

    @Query(value = "select * from user where username regexp ?1 ", nativeQuery = true)
    List<User>getAllAccountByUsername(String searchValue, Pageable pageable);

    @Query(value = "select count(u) from User u")
    int countAllAccount();

    @Query(value = "select u from User u")
    List<User>getAllAccount(Pageable pageable);

}
