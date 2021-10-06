package demo.HotelBooking.repository;

import demo.HotelBooking.entity.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
    Role findByName(String name);

    @Query("select distinct (r.name) from Role r ")
    Set<String> getAllRoles();

}
