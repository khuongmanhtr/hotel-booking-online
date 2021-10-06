package demo.HotelBooking.controller;

import demo.HotelBooking.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class ManagerRestController {

    @Autowired
    BookingCodeService bookingCodeService;

    @Autowired
    RoomService roomService;

    @Autowired
    RoomTypeService roomTypeService;

    @Autowired
    ServiceEntityService serviceEntityService;

    @Autowired
    RoomPromotionCodeService roomPromotionCodeService;

    @Autowired
    ServicePromotionCodeService servicePromotionCodeService;

    @RequestMapping(value = "/manager/bookingCode/getCode", method = POST)
    public Map<Object, Object> getCodeInformation (@RequestBody String code) {
        return bookingCodeService.getBookingCodeInformation(code);
    }

    @RequestMapping(value = "/manager/room/getRoom", method = POST)
    public Map<Object, Object> getRoom(@RequestBody String roomName) {
        return roomService.handleRoomInformation(roomName);
    }

    @RequestMapping(value = "/manager/room/getAllRoom", method = POST)
    public List<Map<Object, Object>> getRoom() {
        return roomService.getAllRoomInHotel();
    }

    @RequestMapping(value = "/manager/room/getRoomTypes", method = POST)
    public List<Map<Object,Object>> getRoomType () {
        return roomTypeService.getAllRoomTypeInformation();
    }

    @RequestMapping(value = "/manager/room/save", method = POST)
    public boolean saveRoom (@RequestBody Map<Object, Object> data) {
        return roomService.saveRoom(data);
    }

    @RequestMapping(value = "/manager/room/delete", method = POST)
    public boolean deleteRoom (@RequestBody int id) {
        return roomService.deleteRoom(id);
    }

    @RequestMapping(value = "/manager/room/allRoomType/delete", method = POST)
    public boolean deleteRoomType(@RequestBody int id) {
        return roomTypeService.deleteRoomType(id);
    }

    @RequestMapping(value = "/manager/service/getService", method = POST)
    public List<Map<Object, Object>> getService() {
        return serviceEntityService.getAllService();
    }

    @RequestMapping(value = "/manager/service/saveService", method = POST)
    public void saveService (@RequestBody Map<Object, Object> data) {
        serviceEntityService.saveService(data);
    }

    @RequestMapping(value = "/manager/service/deleteService", method = POST)
    public void deleteService (@RequestBody int id) {
        serviceEntityService.deleteService(id);
    }

    @RequestMapping(value = "/manager/promoCode/room/save", method = POST)
    public void saveRoomPromoCode(@RequestBody Map<Object, Object> data) {
        roomPromotionCodeService.saveRoomPromoCode(data);
    }

    @RequestMapping(value = "/manager/promoCode/room/delete", method = POST)
    public void deleteRoomPromoCode(@RequestBody int id) {
        roomPromotionCodeService.deleteRoomPromoCode(id);
    }

    @RequestMapping(value = "/manager/promoCode/service/servicePromoDetailList", method = POST)
    public List<Map<Object, Object>> getServicePromoDetailList(@RequestBody int id) {
        return servicePromotionCodeService.getServicePromoDetailList(id);
    }

    @RequestMapping(value = "/manager/promoCode/service/saveServicePromoCode", method = POST)
    public void saveServicePromoCode (@RequestBody Map<Object, Object> data) {
        servicePromotionCodeService.saveServicePromoCode(data);
    }

    @RequestMapping(value = "/manager/promoCode/service/deleteServicePromoCode", method = POST)
    public void deleteServicePromoCode (@RequestBody int id) {
        servicePromotionCodeService.deleteServicePromoCode(id);
    }
}
