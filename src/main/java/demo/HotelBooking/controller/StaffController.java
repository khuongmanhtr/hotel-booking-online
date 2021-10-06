package demo.HotelBooking.controller;

import demo.HotelBooking.entity.*;
import demo.HotelBooking.exporter.CheckOutExporter;
import demo.HotelBooking.exporter.ReportExporter;
import demo.HotelBooking.helper.IRoomCustom;
import demo.HotelBooking.service.BookingCodeService;
import demo.HotelBooking.service.CheckInDetailService;
import demo.HotelBooking.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Controller
public class StaffController {

    @Autowired
    CheckInDetailService checkInDetailService;

    @Autowired
    BookingCodeService bookingCodeService;

    @Autowired
    RoomService roomService;

    //Active Room
    @RequestMapping(value = {"/staff/home/active", "/staff/**"}, method = GET)
    public String activeRoom(@RequestParam(value = "page", required = false) String page,
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
        model.addAttribute("checkInDetailList", result.get("checkInDetailList"));
        model.addAttribute("navigation", navigation);
        model.addAttribute("directory", directory);

        return "staff/staff-home";
    }

    // Booking Code
    @RequestMapping(value = "/staff/home/bookingCode", method = GET)
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
        model.addAttribute("bookingCodeList", result.get("bookingCodeList"));
        model.addAttribute("navigation", navigation);
        model.addAttribute("directory", directory);

        return "staff/staff-bookingCode";
    }

    // Service
    @RequestMapping(value = "/staff/home/service", method = GET)
    public String service(@RequestParam(value = "page", required = false) String page,
                          @RequestParam(value = "filterBy", required = false) String filter,
                          @RequestParam(value = "searchValue", required = false) String searchValue, Model model) {
        String navigation = "home";
        String directory = "service";

        int pageIndex = 1;
        if (page != null) {
            pageIndex = Integer.parseInt(page);
        }

        Map<Object, Object> result = checkInDetailService.handleAllActiveRoomBy(pageIndex, filter, searchValue);
        putAttributeNeededFromMap(model, result);

        model.addAttribute("checkInDetailList", result.get("checkInDetailList"));
        model.addAttribute("serviceDetailInPerRoom", result.get("serviceDetailInPerRoom"));
        model.addAttribute("navigation", navigation);
        model.addAttribute("directory", directory);

        return "staff/staff-service";
    }

    //Check In
    @RequestMapping(value = {"/staff/checkIn/chooseCode", "/staff/checkIn/**"}, method = GET)
    public String chooseRoomForCheckIn (@RequestParam(value = "page", required = false) String page,
                           @RequestParam(value = "filterBy", required = false) String filter,
                           @RequestParam(value = "searchValue", required = false) String searchValue, Model model) {
        String navigation = "checkIn";
        String directory = "chooseCode";

        int pageIndex = 1;
        if (page != null) {
            pageIndex = Integer.parseInt(page);
        }

        Date today = Date.valueOf(LocalDate.now());

        Map<Object, Object> result = bookingCodeService.handleAllBookingCodeNotCheckIn(pageIndex, filter, searchValue);
        putAttributeNeededFromMap(model, result);

        model.addAttribute("bookingCodeList", result.get("bookingCodeList"));
        model.addAttribute("today", today);
        model.addAttribute("navigation", navigation);
        model.addAttribute("directory", directory);

        return "staff/staff-checkIn-s1";
    }

    @RequestMapping(value = "/staff/checkIn/arrange", method = POST)
    public String arrangeForCheckIn (@RequestParam("code") String code, Model model) {
        String navigation = "checkIn";
        BookingCode bookingCode = bookingCodeService.getBookingCodeByCode(code);
        List<BookingDetail> bookingDetailList = new ArrayList<>();

        int totalRoom = 0;

        for (BookingDetail bd : bookingCode.getBookingDetailList()) {
            if (bd.getStatus().getId() == 1 || bd.getStatus().getId() == 2 || bd.getStatus().getId() == 4) {
                totalRoom += bd.getQuantity();
                bookingDetailList.add(bd);
            }
        }

        List<IRoomCustom> allRoomInHotelWithStatus = roomService.getAllRoomInHotelWithStatusBaseOnCode(code);

        model.addAttribute("bookingCode", bookingCode);
        model.addAttribute("bookingDetailList", bookingDetailList);
        model.addAttribute("allRoom", allRoomInHotelWithStatus);
        model.addAttribute("totalRoom", totalRoom);
        model.addAttribute("navigation", navigation);

        return "staff/staff-checkIn-s2";
    }

    @RequestMapping(value = "/staff/checkIn/finish", method = POST)
    public String finishCheckIn (@RequestParam("roomName") String[] rooms, @RequestParam("bookingCode") String code, Model model) {
        String navigation = "checkIn";

        BookingCode bookingCode = bookingCodeService.getBookingCodeByCode(code);

        Arrays.sort(rooms);
        List<Room> roomList = new ArrayList<>();
        for (int i = 0 ; i < rooms.length; i ++) {
            Room room = roomService.getRoomByName(rooms[i]);
            roomList.add(room);
        }

        List<BookingDetail> bookingDetailList = bookingCode.getBookingDetailList();
        int totalRoom = 0;
        for (BookingDetail bd : bookingDetailList) {
            totalRoom += bd.getQuantity();
        }

        model.addAttribute("bookingCode", bookingCode);
        model.addAttribute("totalRoom", totalRoom);
        model.addAttribute("roomList", roomList);
        model.addAttribute("navigation", navigation);

        return "staff/staff-checkIn-s3";
    }

    @RequestMapping(value = {"/staff/checkOut/chooseCode", "/staff/checkOut/**"}, method = GET)
    public String chooseRoomForCheckOut (@RequestParam(value = "page", required = false) String page,
                                         @RequestParam(value = "filterBy", required = false) String filter,
                                         @RequestParam(value = "searchValue", required = false) String searchValue, Model model) {
        String navigation = "checkOut";
        String directory = "chooseCode";

        int pageIndex = 1;
        if (page != null) {
            pageIndex = Integer.parseInt(page);
        }

        Map<Object, Object> result = bookingCodeService.handleAllBookingCodeCheckIn(pageIndex, filter, searchValue);
        putAttributeNeededFromMap(model, result);

        model.addAttribute("bookingCodeList", result.get("bookingCodeList"));
        model.addAttribute("navigation", navigation);
        model.addAttribute("directory", directory);

        return "staff/staff-checkOut-s1";
    }

    @RequestMapping(value = "/staff/checkOut/confirm", method = POST)
    public String confirmCheckOut (@RequestParam("bookingCode") String code, Model model) {
        String navigation = "checkOut";

        Map<Object, Object> result = bookingCodeService.getAllInformationToCheckOut(code);

        model.addAttribute("navigation", navigation);
        model.addAttribute("promoCode", result.get("promoCode"));
        model.addAttribute("promoType", result.get("promoType"));
        model.addAttribute("bookingCode", result.get("bookingCode"));
        model.addAttribute("checkInDetailList", result.get("checkInDetailList"));
        model.addAttribute("serviceDetailInPerRoom", result.get("serviceDetailInPerRoom"));
        model.addAttribute("allTotalService", result.get("allTotalService"));

        return "staff/staff-checkOut-s2";
    }

    @RequestMapping(value = "/staff/checkOut/printPdf", method = GET)
    public void exportPDF (@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");

        Map<Object, Object> result = bookingCodeService.getAllInformationToCheckOut(code);

        String headerKey = "Content Disposition";
        String headerValue = "attachment; filename=checkout.pdf";

        response.setHeader(headerKey, headerValue);
        CheckOutExporter exporter = new CheckOutExporter(result);
        exporter.export(response);
    }

    @RequestMapping(value = "/staff/report", method = GET)
    public String reportPage (Model model) {
        String navigation = "report";

        model.addAttribute("navigation", navigation);

        return "staff/staff-report";
    }

    @RequestMapping(value = "/staff/report/export", method = GET)
    public void reportByExportPdf (@RequestParam("dateFrom") String dateFrom, @RequestParam("dateTo") String dateTo,
                                   HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");

        Date dateFromCheck = Date.valueOf(dateFrom);
        Date dateToCheck = Date.valueOf(dateTo);

        List<BookingCode> bookingCodes = bookingCodeService.getBookingCodesFromDateToDate(dateFromCheck, dateToCheck);

        String headerKey = "Content Disposition";
        String headerValue = "attachment; filename=checkout.pdf";

        response.setHeader(headerKey, headerValue);
        ReportExporter exporter = new ReportExporter(bookingCodes, dateFromCheck, dateToCheck);
        exporter.export(response);
    }

    public static void putAttributeNeededFromMap(Model model, Map<Object, Object> map) {
        model.addAttribute("maxPage", map.get("maxPage"));
        model.addAttribute("minPage", map.get("minPage"));
        model.addAttribute("pageIndex",  map.get("pageIndex"));
        model.addAttribute("paginationList", map.get("paginationList"));
        model.addAttribute("indexOfPageIndex", map.get("indexOfPageIndex"));
        model.addAttribute("options", map.get("options"));
        model.addAttribute("filter", map.get("filter"));
        model.addAttribute("searchValue", map.get("searchValue"));
    }

}
