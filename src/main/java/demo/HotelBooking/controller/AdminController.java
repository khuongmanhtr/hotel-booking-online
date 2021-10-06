package demo.HotelBooking.controller;

import demo.HotelBooking.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class AdminController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/admin/**", method = GET)
    public String admin(@RequestParam(value = "page", required = false) String page,
                        @RequestParam(value = "filterBy", required = false) String filter,
                        @RequestParam(value = "searchValue", required = false) String searchValue, Model model) {
        int pageIndex = 1;
        if (page != null) {
            pageIndex = Integer.parseInt(page);
        }

        Map<Object, Object> result = userService.handleAllAccount(pageIndex, filter, searchValue);

        model.addAttribute("userList", result.get("userList"));
        model.addAttribute("maxPage", result.get("maxPage"));
        model.addAttribute("minPage", result.get("minPage"));
        model.addAttribute("pageIndex",  result.get("pageIndex"));
        model.addAttribute("paginationList", result.get("paginationList"));
        model.addAttribute("indexOfPageIndex", result.get("indexOfPageIndex"));
        model.addAttribute("options", result.get("options"));
        model.addAttribute("filter", result.get("filter"));
        model.addAttribute("searchValue", result.get("searchValue"));

        return "admin/admin-home";
    }
}
