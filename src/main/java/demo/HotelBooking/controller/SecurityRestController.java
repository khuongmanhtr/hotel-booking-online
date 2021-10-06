package demo.HotelBooking.controller;

import demo.HotelBooking.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class SecurityRestController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/changePassword", method = POST)
    public boolean changePassword (@RequestBody Map<Object, Object> userMap){
        String username = (String) userMap.get("username");
        String password = (String) userMap.get("password");
        String retypePassword = (String) userMap.get("retype");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        if (username.equals(currentPrincipalName)) {
            if (password.equals(retypePassword)) {
                return userService.changePassword(username, password);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
