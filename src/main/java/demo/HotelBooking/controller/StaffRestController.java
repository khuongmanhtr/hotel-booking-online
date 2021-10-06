package demo.HotelBooking.controller;

import demo.HotelBooking.service.BookingCodeService;
import demo.HotelBooking.service.CheckInDetailService;
import demo.HotelBooking.service.ServicePerRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class StaffRestController {

    @Autowired
    BookingCodeService bookingCodeService;

    @Autowired
    CheckInDetailService checkInDetailService;

    @Autowired
    ServicePerRoomService servicePerRoomService;

    @RequestMapping(value = "/staff/bookingCode/getCode", method = POST)
    public Map<Object, Object> getCodeInformation (@RequestBody String code) {
        return bookingCodeService.getBookingCodeInformation(code);
    }

    @RequestMapping(value = "/staff/service/getRoom", method = POST)
    public Map<Object, Object> getRoomByNameAndCode(@RequestBody Map<String, String> data) {
        return checkInDetailService.getRoomByRoomAndCode(data);
    }

    @RequestMapping(value = "/staff/service/updateService", method = POST)
    public boolean updateService(@RequestBody Map<Object, Object> data) {
        return servicePerRoomService.updateService(data);
    }

    @RequestMapping(value = "/staff/checkIn/updateData", method = POST)
    public boolean saveCheckInDetail(@RequestBody Map<Object, Object> data) {
        return checkInDetailService.checkIn(data);
    }

    @RequestMapping(value = "/staff/checkOut/updateData", method = POST)
    public boolean saveCheckOutDetail(@RequestBody String bookingCode) {
        return bookingCodeService.checkOut(bookingCode);
    }


}
