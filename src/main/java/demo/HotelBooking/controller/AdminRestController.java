package demo.HotelBooking.controller;

import demo.HotelBooking.security.UserService;
import demo.HotelBooking.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
public class AdminRestController {
    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @RequestMapping(value = "/admin/getAllRoles", method = POST)
    public Set<String> getAllRoles() {
        return roleService.getAllRoles();
    }

    @RequestMapping(value = "/admin/createAccount", method = POST)
    public void createAccount(@RequestBody Map<Object, Object> account) {
        userService.saveAccount(account);
    }

    @RequestMapping(value = "/admin/getUser", method = POST)
    public Map<Object, Object> getUserInformation(@RequestBody String username) {
        return userService.getUserInformation(username);
    }

    @RequestMapping(value = "/admin/update", method = POST)
    public void updateAccount(@RequestBody Map<Object, Object> data) {
        userService.updateUser(data);
    }

    @RequestMapping(value = "/admin/delete", method = POST)
    public void deleteAccount(@RequestBody String username) {
        userService.deleteUser(username);
    }
}
