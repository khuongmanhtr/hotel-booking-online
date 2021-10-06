package demo.HotelBooking.controller;

import demo.HotelBooking.entity.*;
import demo.HotelBooking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.sql.Date;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Controller
public class HomeController {
    @Autowired
    RoomTypeService roomTypeService;

    @Autowired
    BookingCodeService bookingCodeService;

    @Autowired
    BookingDetailService bookingDetailService;

    @Autowired
    CustomerService customerService;

    @Autowired
    HotelBankAccountService hotelBankAccountService;

    @Autowired
    RoomPromotionCodeService roomPromotionCodeService;

    @RequestMapping(value = "/room/{roomType}", method = GET)
    public String roomTypeInformation(@PathVariable("roomType") String roomTypeName, Model model) {

        RoomType roomType = roomTypeService.getRoomTypeByName(roomTypeName);
        model.addAttribute("roomType", roomType);
        model.addAttribute("roomTypeImageList", roomType.getRoomTypeImageList());

        getRoomTypeListForHeader(model);

        return "single-room";
    }

    @RequestMapping(value = "/checkCode", method = GET)
    public String checkBookingCode(@RequestParam("booking-code") String code, Model model) {
        BookingCode bookingCode = bookingCodeService.getBookingCodeByCode(code.trim());
        List<BookingDetail> bookingDetailList;
        if (bookingCode == null) {
            bookingDetailList = null;
        } else {
            bookingDetailList= bookingCode.getBookingDetailList();
            String today = Date.valueOf(LocalDate.now()).toString();

            String dateFrom = bookingCode.getDateFrom().toString();
            String dateTo = bookingCode.getDateTo().toString();
            long night = diffDateBetweenTwoStringDate(dateFrom, dateTo);

            long betweenDate = diffDateBetweenTwoStringDate(today, dateFrom);
            boolean isNotAllowCancellable = true;
            if (betweenDate > 3) {
                isNotAllowCancellable = false;
            }

            String formattedDateFrom = convertShortDateToFullDate(dateFrom);
            String formattedDateTo = convertShortDateToFullDate(dateTo);


            model.addAttribute("formattedDateFrom",formattedDateFrom);
            model.addAttribute("formattedToFrom",formattedDateTo);
            model.addAttribute("night", night);
            model.addAttribute("notAllowCancellable", isNotAllowCancellable);
        }

        model.addAttribute("bookingCode", code);
        model.addAttribute("bookingDetails", bookingDetailList);
        getRoomTypeListForHeader(model);

        return "booking-code";
    }

    @RequestMapping(value = {"/book/chooseRoom", "/book/**"}, method = GET)
    public String chooseRoom (Model model) {

        Date today = Date.valueOf(LocalDate.now());

        model.addAttribute("today", today);
        model.addAttribute("notification", "please choose date to check available room");

        getRoomTypeListForHeader(model);

        return "choose-room";
    }

    @RequestMapping(value = "/book/chooseRoom", method = POST)
    public String checkDateBooking (@RequestParam("dateFrom") String dateFrom,
                                    @RequestParam("dateTo") String dateTo,
                                    Model model) {

        Date today = Date.valueOf(LocalDate.now());
        Date dateFromCheck = Date.valueOf(dateFrom);
        Date dateToCheck = Date.valueOf(dateTo);

        List<RoomType> availableRoomTypes = roomTypeService.getAvailableRoomTypes(dateFromCheck, dateToCheck);

        model.addAttribute("today", today);
        model.addAttribute("roomTypes", availableRoomTypes);
        model.addAttribute("dateFromCheck",dateFromCheck);
        model.addAttribute("dateToCheck",dateToCheck);
        model.addAttribute("message","sorry all rooms are full please check another date");
        getRoomTypeListForHeader(model);

        return "choose-room";
    }

    @RequestMapping(value = "/book/bookRoom", method = POST)
    public String selectRoomQuantity (@RequestParam("roomType") List<String> roomTypes,
                                      @RequestParam("dateFrom") String dateFrom,
                                      @RequestParam("dateTo") String dateTo,
                                      Model model) {
        Date dateFromCheck = Date.valueOf(dateFrom);
        Date dateToCheck = Date.valueOf(dateTo);
        long dateDiff = diffDateBetweenTwoStringDate(dateFrom, dateTo);

        Map<RoomType, Long> roomTypeMap = roomTypeService.getRoomTypeMap(roomTypes, dateFromCheck, dateToCheck);

        model.addAttribute("roomTypeMap", roomTypeMap);
        model.addAttribute("dateFromCheck",dateFromCheck);
        model.addAttribute("dateToCheck",dateToCheck);
        model.addAttribute("night", dateDiff);
        getRoomTypeListForHeader(model);

        return "booking-room";
    }

    @RequestMapping(value = "/book/confirm", method = POST)
    public String confirmBooking (@RequestParam("name") String[] names,
                            @RequestParam("quantity") long[] quantityList,
                            @RequestParam("dateFrom") String dateFrom,
                            @RequestParam("dateTo") String dateTo,
                            Model model) {
        Date dateFromCheck = Date.valueOf(dateFrom);
        Date dateToCheck = Date.valueOf(dateTo);

        long dateDiff = diffDateBetweenTwoStringDate(dateFrom, dateTo);

        String formattedDateFrom = convertShortDateToFullDate(dateFrom);
        String formattedDateTo = convertShortDateToFullDate(dateTo);

        Map<RoomType, Long> roomTypeMap = new LinkedHashMap();

        for (int i = 0; i < names.length; i++) {
            RoomType roomType = roomTypeService.getRoomTypeByName(names[i]);
            roomTypeMap.put(roomType, quantityList[i]);
        }

        model.addAttribute("formattedDateFrom",formattedDateFrom);
        model.addAttribute("formattedDateTo",formattedDateTo);
        model.addAttribute("dateFromCheck",dateFromCheck);
        model.addAttribute("dateToCheck",dateToCheck);
        model.addAttribute("night", dateDiff);
        model.addAttribute("roomTypeMap", roomTypeMap);

        getRoomTypeListForHeader(model);

        return "confirm-booking";
    }

    @RequestMapping(value = "/book/payment", method = POST)
    public String checkPayment (@RequestParam("name") String[] names, @RequestParam("quantity") long[] quantityList,
                                @RequestParam("dateFrom") String dateFrom, @RequestParam("dateTo") String dateTo,
                                @RequestParam("promoCode") String promoCode, @RequestParam("promoType") String promoType,
                                @RequestParam("customerName") String customerName, @RequestParam("customerAccount") String customerAccount,
                                @RequestParam("transferMessage") String transferMessage, Model model) {
        HotelBankAccount hotelAccount = hotelBankAccountService.getBankAccountById(1);

        long nights = diffDateBetweenTwoStringDate(dateFrom, dateTo);
        double totalPayment = 0;

        totalPayment = roomTypeService.calculateTotalByRoomTypesAndQuantityList(names, quantityList, totalPayment);
        totalPayment = roomPromotionCodeService.applyDiscountToPayment(promoType, promoCode, totalPayment);
        totalPayment *= nights;

        BankAccountTransfer transfer = new BankAccountTransfer(customerAccount, hotelAccount.getBankAccount(), totalPayment, transferMessage);

        String url = "http://localhost:7777/transfer/";
        RestTemplate restTemplate = new RestTemplate();
        Map<Object, Object> responseMap = restTemplate.postForObject(url,transfer, Map.class);
        assert responseMap != null;
        boolean responseValue = (boolean) responseMap.get("isValid");

        String newCode = "";

        if (responseValue) {
            newCode = bookingCodeService.generateBookingCode();
            BookingCode bookingCode =  bookingCodeService.saveData(newCode, dateFrom, dateTo);
            bookingDetailService.saveData(bookingCode, names, quantityList);
            customerService.saveData(bookingCode, customerName, customerAccount, totalPayment, promoCode, promoType);
        }

        model.addAttribute("bookingCode", newCode);
        model.addAttribute("customerName", customerName);
        model.addAttribute("status", responseValue);
        getRoomTypeListForHeader(model);

        return "payment";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf,true));
    }

    public void getRoomTypeListForHeader(Model model) {
        List<RoomType> roomTypeList = roomTypeService.getAllRoomType();
        model.addAttribute("roomTypeList", roomTypeList);
    }

    public String convertShortDateToFullDate (String date) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(Locale.ENGLISH);

        LocalDate localDate = LocalDate.parse(date, inputFormatter);
        return outputFormatter.format(localDate);
    }

    public long diffDateBetweenTwoStringDate(String date1, String date2) {
        LocalDate localDate1 = LocalDate.parse(date1);
        LocalDate localDate2 = LocalDate.parse(date2);
        return DAYS.between(localDate1, localDate2);
    }

}
