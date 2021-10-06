package demo.HotelBooking.controller;

import demo.HotelBooking.entity.RoomType;
import demo.HotelBooking.service.RoomTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class SecurityController {
    @Autowired
    RoomTypeService roomTypeService;

    @RequestMapping("/")
    public String homepage (Model model) {
        List<RoomType> roomTypeList = roomTypeService.getAllRoomType();

        Date today = Date.valueOf(LocalDate.now());

        model.addAttribute("today", today);
        model.addAttribute("roomTypeList", roomTypeList);
        return "homepage";
    }

    @RequestMapping("/signIn")
    public String signIn () {
        return "sign-in";
    }

//    @RequestMapping(value = "/login", method = POST)
//    public String login () {
//        return "/";
//    }

    @RequestMapping(value = "/loginError")
    public String logInError (Model model) {
        String errorMessage = "Invalid username and password, Please try again !";
        model.addAttribute("errorMessage", errorMessage);
        return "sign-in";
    }

    @RequestMapping(value = "/forbidden")
    private String forbiddenError () {
        return "403";
    }

    @RequestMapping(value = "/changePassword", method = GET)
    public String changePasswordPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        model.addAttribute("principal", currentPrincipalName);

        return "change-password";
    }
}
