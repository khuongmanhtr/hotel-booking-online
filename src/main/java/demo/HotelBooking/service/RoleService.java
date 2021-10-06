package demo.HotelBooking.service;

import demo.HotelBooking.entity.Role;
import demo.HotelBooking.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleService {
    @Autowired
    RoleRepository roleRepository;

    public Set<String> getAllRoles() {
        return roleRepository.getAllRoles();
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }
}
