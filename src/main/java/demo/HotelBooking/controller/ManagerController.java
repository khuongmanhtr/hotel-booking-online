package demo.HotelBooking.controller;

import demo.HotelBooking.entity.RoomType;
import demo.HotelBooking.helper.IRoomCustom;
import demo.HotelBooking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class ManagerController {

    @Autowired
    CheckInDetailService checkInDetailService;

    @Autowired
    BookingCodeService bookingCodeService;

    @Autowired
    CustomerService customerService;

    @Autowired
    BookingDetailService bookingDetailService;

    @Autowired
    HotelBankAccountService hotelBankAccountService;

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

    // Active Room
    @RequestMapping(value = {"/manager","/manager/**", "/manager/home/active"}, method = GET)
    public String activeRoom (@RequestParam(value = "page", required = false) String page,
                              @RequestParam(value = "filterBy", required = false) String filter,
                              @RequestParam(value = "searchValue", required = false) String searchValue, Model model) {
        String navigation = "home";
        String directory = "active";

        int pageIndex = 1;
        if (page != null) {
            pageIndex = Integer.parseInt(page);
        }

        Map<Object, Object> result = checkInDetailService.handleAllActiveRoomBy(pageIndex, filter, searchValue);

        putAttributeNeededFromMap(model, result);
        getAllCustomerRequest(model);

        model.addAttribute("checkInDetailList", result.get("checkInDetailList"));
        model.addAttribute("navigation", navigation);
        model.addAttribute("directory", directory);

        return "manager/manager-home";
    }

    // Booking Code
    @RequestMapping(value = "/manager/home/bookingCode", method = GET)
    public String bookingCode(@RequestParam(value = "page", required = false) String page,
                              @RequestParam(value = "filterBy", required = false) String filter,
                              @RequestParam(value = "searchValue", required = false) String searchValue, Model model) {
        String navigation = "home";
        String directory = "bookingCode";

        int pageIndex = 1;
        if (page != null) {
            pageIndex = Integer.parseInt(page);
        }

        Map<Object, Object> result = bookingCodeService.handleAllBookingCodeBy(pageIndex, filter, searchValue);

        putAttributeNeededFromMap(model, result);
        getAllCustomerRequest(model);

        model.addAttribute("bookingCodeList", result.get("bookingCodeList"));
        model.addAttribute("navigation", navigation);
        model.addAttribute("directory", directory);

        return "manager/manager-bookingCode";
    }

    // Customer
    @RequestMapping(value = "/manager/home/customer", method = GET)
    public String customer (@RequestParam(value = "page", required = false) String page,
                            @RequestParam(value = "filterBy", required = false) String filter,
                            @RequestParam(value = "searchValue", required = false) String searchValue ,Model model) {
        String navigation = "home";
        String directory = "customer";

        int pageIndex = 1;
        if (page != null) {
            pageIndex = Integer.parseInt(page);
        }

        Map<Object, Object> result = customerService.handleAllCustomerBy(pageIndex, filter, searchValue);

        putAttributeNeededFromMap(model, result);
        getAllCustomerRequest(model);

        model.addAttribute("customerList", result.get("customerList"));
        model.addAttribute("navigation", navigation);
        model.addAttribute("directory", directory);

        return "manager/manager-customer";
    }

    //Customer Request
    @RequestMapping(value = {"/manager/request/chooseRequest", "/manager/request/**"}, method = GET)
    public String customerRequest (@RequestParam(value = "page", required = false) String page, Model model) {
        String navigation = "request";
        String directory = "chooseRequest";

        int pageIndex = 1;
        if (page != null) {
            pageIndex = Integer.parseInt(page);
        }

        Map<Object, Object> result = bookingDetailService.getAllCustomerRequest(pageIndex);

        putAttributeNeededFromMap(model, result);
        getAllCustomerRequest(model);

        model.addAttribute("navigation", navigation);
        model.addAttribute("directory", directory);
        model.addAttribute("bookingDetailList", result.get("bookingDetailList"));

        return "manager/manager-request-s1";
    }

    // Confirm customer request
    @RequestMapping(value = "/manager/request/confirmRequest", method = POST)
    public String confirmRequest (@RequestParam("bookingCode") String code, @RequestParam("roomType") String roomType, Model model) {
        String navigation = "request";

        Map<Object, Object> result = bookingDetailService.getRequestedCancelBookingDetail(code, roomType);

        getAllCustomerRequest(model);
        model.addAttribute("navigation", navigation);

        model.addAttribute("promoCode",result.get("promoCode"));
        model.addAttribute("promoType",result.get("promoType"));
        model.addAttribute("bookingCode",result.get("bookingCode"));
        model.addAttribute("bookingDetail",result.get("bookingDetail"));
        model.addAttribute("hotelBankAccount",result.get("hotelBankAccount"));
        model.addAttribute("totalRoom", result.get("totalRoom"));
        model.addAttribute("bookingDetailList", result.get("bookingDetailList"));
        model.addAttribute("numberOfNight", result.get("numberOfNight"));

        return "manager/manager-request-s2";
    }

    // Decline request
    @RequestMapping(value = "/manager/request/decline", method = POST)
    public String declineRequest(@RequestParam("bookingCode") String code, @RequestParam("roomType") String roomType, Model model) {
        String navigation = "request";
        Map<Object, Object> data = new LinkedHashMap<>();
        data.put("roomType", roomType);
        data.put("bookingCode", code);
        boolean isDeclined = bookingDetailService.changeBookingCodeStatus(data, 4);
        if (isDeclined) {
            model.addAttribute("boxType", "decline");
        } else {
            model.addAttribute("boxType", "failure");
        }

        getAllCustomerRequest(model);
        model.addAttribute("navigation", navigation);

        return "manager/manager-request-s3";
    }

    //Accept request
    @RequestMapping(value = "/manager/request/accept", method = POST)
    public String acceptRequest(@RequestParam("bookingCode") String bookingCode, @RequestParam("roomType") String roomType, Model model) {
        String navigation = "request";

        Map<Object,Object> result = bookingDetailService.handleAcceptedRefundRequest(bookingCode, roomType);

        boolean isSuccess = (boolean) result.get("isSuccess");
        if (isSuccess) {
            model.addAttribute("boxType", "success");
            model.addAttribute("bookingCode", result.get("bookingCodeObject"));
            model.addAttribute("totalRefund", result.get("totalRefund"));
        } else {
            model.addAttribute("boxType", "failure");
        }

        getAllCustomerRequest(model);
        model.addAttribute("navigation", navigation);

        return "manager/manager-request-s3";
    }

    // View & edit all room
    @RequestMapping(value = {"/manager/room/allRoom","/manager/room/**"}, method = GET)
    public String viewAllRoom(Model model) {
        String navigation = "room";

        Map<Object, Object> result = roomTypeService.handleAllRoomTypeParameter();

        List<IRoomCustom> allRoomInHotelWithStatus = roomService.getAllRoomInHotelWithStatus();

        getAllCustomerRequest(model);
        model.addAttribute("navigation", navigation);
        model.addAttribute("allRoom", allRoomInHotelWithStatus);
        model.addAttribute("allRoomTypeInfo", result);

        return "manager/manager-room";
    }

    // View & edit all room type
    @RequestMapping(value = "/manager/room/allRoomType", method = GET)
    public String viewAllRoomType(@RequestParam(value = "page", required = false) String page,
                                  @RequestParam(value = "filterBy", required = false) String filter,
                                  @RequestParam(value = "searchValue", required = false) String searchValue, Model model) {
        String navigation = "room";
        String directory = "allRoomType";

        int pageIndex = 1;
        if (page != null) {
            pageIndex = Integer.parseInt(page);
        }

        Map<Object, Object> result = roomTypeService.handleAllRoomType(pageIndex, filter, searchValue);

        putAttributeNeededFromMap(model, result);
        getAllCustomerRequest(model);

        model.addAttribute("allRoomType", result.get("allRoomType"));
        model.addAttribute("navigation", navigation);
        model.addAttribute("directory", directory);

        return "manager/manager-roomType";
    }

    // Create Room Type Form
    @RequestMapping(value = "/manager/room/createRoomType", method = POST)
    public String createRoomType(@RequestParam(value ="roomTypeId", required = false, defaultValue = "0") int id, Model model) {
        String navigation = "room";

        RoomType roomType = roomTypeService.getRoomTypeById(id);

        if (roomType != null) {
            model.addAttribute("roomType", roomType);
            model.addAttribute("title", "edit room type");
        } else {
            model.addAttribute("title", "create new room type");
        }

        getAllCustomerRequest(model);
        model.addAttribute("navigation", navigation);

        return "manager/manager-createRoomType";
    }

    // Save form
    @RequestMapping(value = "/manager/room/createRoomType/save", method = POST)
    public String saveRoomType(@RequestParam(value = "typeId" ,required = false, defaultValue = "0") int id,
                               @RequestParam("typeName") String roomTypeName,
                               @RequestParam("numberOfPeople") String numberOfPeople,
                               @RequestParam("typePrice") String price,
                               @RequestParam("typeDesc") String description,
                               @RequestParam(value = "typeImage", required = false) MultipartFile imageFile) {
        try {
            roomTypeService.saveData(id, roomTypeName, numberOfPeople, price, description, imageFile);
        } catch (IOException e){
            e.printStackTrace();
        }
        return "redirect:/manager/room/allRoomType";
    }

    //Services
    @RequestMapping(value = "/manager/service/**", method = GET)
    public String viewAllService(@RequestParam(value = "page", required = false) String page,
                                 @RequestParam(value = "filterBy", required = false) String filter,
                                 @RequestParam(value = "searchValue", required = false) String searchValue, Model model) {
        String navigation = "service";
        String directory = "allRoomType";

        int pageIndex = 1;
        if (page != null) {
            pageIndex = Integer.parseInt(page);
        }

        Map<Object, Object> result = serviceEntityService.handAllService(pageIndex, filter, searchValue);
        putAttributeNeededFromMap(model, result);
        getAllCustomerRequest(model);
        model.addAttribute("serviceList", result.get("serviceList"));
        model.addAttribute("navigation", navigation);
        model.addAttribute("directory", directory);

        return "manager/manager-services";
    }

    // Room Promotion Code
    @RequestMapping(value = {"/manager/promoCode/room","/manager/promoCode/**"}, method = GET)
    public String viewAllRoomPromoCode (@RequestParam(value = "page", required = false) String page,
                                        @RequestParam(value = "filterBy", required = false) String filter,
                                        @RequestParam(value = "searchValue", required = false) String searchValue, Model model) {
        String navigation = "promotionCode";
        String directory = "room";

        int pageIndex = 1;
        if (page != null) {
            pageIndex = Integer.parseInt(page);
        }

        Map<Object, Object> result = roomPromotionCodeService.handleAllRoomPromoCode(pageIndex, filter, searchValue);
        putAttributeNeededFromMap(model, result);

        getAllCustomerRequest(model);
        model.addAttribute("promoCodeList", result.get("promoCodeList"));
        model.addAttribute("navigation", navigation);
        model.addAttribute("directory", directory);

        return "manager/manager-roomPromotion";
    }

    // Service Promotion Code
    @RequestMapping(value = "/manager/promoCode/service", method = GET)
    public String viewAllServicePromoCode (@RequestParam(value = "page", required = false) String page,
                                           @RequestParam(value = "filterBy", required = false) String filter,
                                           @RequestParam(value = "searchValue", required = false) String searchValue, Model model) {
        String navigation = "promotionCode";
        String directory = "service";

        int pageIndex = 1;
        if (page != null) {
            pageIndex = Integer.parseInt(page);
        }

        Map<Object, Object> result = servicePromotionCodeService.handleAllServicePromoCode(pageIndex, filter, searchValue);
        putAttributeNeededFromMap(model, result);

        getAllCustomerRequest(model);
        model.addAttribute("promoCodeList", result.get("promoCodeList"));
        model.addAttribute("navigation", navigation);
        model.addAttribute("directory", directory);

        return "manager/manager-servicePromotion";
    }

    public void putAttributeNeededFromMap(Model model, Map<Object, Object> map) {
        model.addAttribute("maxPage", map.get("maxPage"));
        model.addAttribute("minPage", map.get("minPage"));
        model.addAttribute("pageIndex",  map.get("pageIndex"));
        model.addAttribute("paginationList", map.get("paginationList"));
        model.addAttribute("indexOfPageIndex", map.get("indexOfPageIndex"));
        model.addAttribute("options", map.get("options"));
        model.addAttribute("filter", map.get("filter"));
        model.addAttribute("searchValue", map.get("searchValue"));
    }

    public void getAllCustomerRequest(Model model) {
        int requestQuantity = bookingDetailService.getAllCustomerRequestQuantity();
        model.addAttribute("requestQuantity", requestQuantity);
    }

}
