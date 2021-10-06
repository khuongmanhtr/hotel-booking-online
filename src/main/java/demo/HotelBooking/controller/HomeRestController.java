package demo.HotelBooking.controller;

import demo.HotelBooking.service.BookingDetailService;
import demo.HotelBooking.service.RoomPromotionCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.bind.annotation.RequestMethod.*;

import java.util.Map;

@RestController
public class HomeRestController {

    @Autowired
    RoomPromotionCodeService roomPromotionCodeService;

    @Autowired
    BookingDetailService bookingDetailService;

    @RequestMapping(value = "/getCode", method = POST)
    public Map<Object, Object> getPromotionCode (@RequestBody String code) {
        return roomPromotionCodeService.getServiceCodeOrRoomCode(code);
    }

    @RequestMapping(value = "/cancelRoom", method = POST)
    public boolean sendCancelRequest (@RequestBody Map<Object,Object> data) {
        return bookingDetailService.changeBookingCodeStatus(data, 2);
    }
}
