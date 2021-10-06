package demo.HotelBooking.service;

import demo.HotelBooking.entity.*;
import demo.HotelBooking.helper.ResultPagination;
import demo.HotelBooking.repository.BookingDetailRepository;
import demo.HotelBooking.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class BookingDetailService {
    @Autowired
    BookingDetailRepository bookingDetailRepository;

    @Autowired
    RoomTypeService roomTypeService;

    @Autowired
    StatusRepository statusRepository;

    @Autowired
    BookingCodeService bookingCodeService;

    @Autowired
    HotelBankAccountService hotelBankAccountService;

    public void saveData(BookingCode bookingCode, String[] names, long[] quantityList) {
        for (int i = 0; i < names.length; i++) {
            RoomType roomType = roomTypeService.getRoomTypeByName(names[i]);
            Status status = statusRepository.findById(1);
            BookingDetail bookingDetail = new BookingDetail();
            bookingDetail.setBookingCode(bookingCode);
            bookingDetail.setRoomType(roomType);
            bookingDetail.setQuantity((int) quantityList[i]);
            bookingDetail.setStatus(status);
            bookingDetailRepository.save(bookingDetail);
        }
    }

    public boolean changeBookingCodeStatus (Map<Object,Object> data, int statusId) {
        String roomType = (String) data.get("roomType");
        String bookingCode = (String) data.get("bookingCode");
        BookingDetail bookingDetail = bookingDetailRepository.getBookingDetailByBookingCodeAndRoomType(bookingCode, roomType);

        if (bookingDetail != null) {
            Status status = statusRepository.findById(statusId);
            bookingDetail.setStatus(status);
            bookingDetailRepository.save(bookingDetail);
            return true;
        } else
            return false;
    }

    public int getAllCustomerRequestQuantity() {
        return bookingDetailRepository.countAllCustomerRequest();
    }

    public Map<Object,Object> getAllCustomerRequest (int pageIndex) {
        Map<Object, Object> map = new LinkedHashMap<>();
        List<BookingDetail> bookingDetailList = new ArrayList<>();
        List<Integer> paginationList = new ArrayList<>();
        int sizePerPage = 15;
        int rangePage = 5;
        int minPage = 1;
        int maxPage = 0;
        int resultQuantity = 0;
        int indexOfPageIndex = 0;

        resultQuantity = bookingDetailRepository.countAllCustomerRequest();
        maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
        paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
        indexOfPageIndex = paginationList.indexOf(pageIndex);
        bookingDetailList = bookingDetailRepository.getAllCustomerRequest(PageRequest.of(pageIndex - 1, sizePerPage));

        map.put("resultQuantity", resultQuantity);
        map.put("maxPage", maxPage);
        map.put("minPage", minPage);
        map.put("pageIndex", pageIndex);
        map.put("paginationList", paginationList);
        map.put("indexOfPageIndex", indexOfPageIndex);
        map.put("bookingDetailList", bookingDetailList);
        return map;
    }

    public Map<Object,Object> getRequestedCancelBookingDetail(String code, String roomType) {
        Map<Object, Object> map = new LinkedHashMap<>();
        BookingCode bookingCode = bookingCodeService.getBookingCodeByCode(code);
        List<BookingDetail> bookingDetailList = bookingCode.getBookingDetailList();

        BookingDetail bookingDetail = bookingDetailRepository.getBookingDetailByBookingCodeAndRoomType(code, roomType);
        HotelBankAccount hotelBankAccount = hotelBankAccountService.getBankAccountById(1);

        int totalRoom = 0;
        for (BookingDetail bd : bookingDetailList) {
            totalRoom += bd.getQuantity();
        }

        long dateDiff = getDateDiff(bookingCode.getDateFrom(),bookingCode.getDateTo(),TimeUnit.DAYS);

        RoomPromotionCode roomPromotionCode = bookingCode.getCustomer().getRoomPromotionCode();
        ServicePromotionCode servicePromotionCode = bookingCode.getCustomer().getServicePromotionCode();
        if (roomPromotionCode != null) {
            map.put("promoCode", roomPromotionCode);
            map.put("promoType", "Room Discount");
        } else if (servicePromotionCode != null) {
            map.put("promoCode", servicePromotionCode);
            map.put("promoType", "Service Discount");
        } else {
            map.put("promoCode", null);
            map.put("promoType", null);
        }

        map.put("bookingCode", bookingCode);
        map.put("bookingDetail", bookingDetail);
        map.put("hotelBankAccount", hotelBankAccount);
        map.put("totalRoom", totalRoom);
        map.put("bookingDetailList", bookingDetailList);
        map.put("numberOfNight",dateDiff);

        return map;
    }

    public Map<Object, Object> handleAcceptedRefundRequest(String code, String roomType) {
        BookingDetail bookingDetail = bookingDetailRepository.getBookingDetailByBookingCodeAndRoomType(code,roomType);
        BookingCode bookingCode = bookingDetail.getBookingCode();

        // Hotel Bank Account
        HotelBankAccount hotelAccount = hotelBankAccountService.getBankAccountById(1);

        // Customer Bank Account
        String customerAccount = bookingCode.getCustomer().getBankAccount();

        // Calculate Total Refund
        long nights = getDateDiff(bookingCode.getDateFrom(), bookingCode.getDateTo(),TimeUnit.DAYS);
        double totalRefund = bookingDetail.getRoomType().getPrice() * bookingDetail.getQuantity();

        RoomPromotionCode roomPromotionCode = bookingCode.getCustomer().getRoomPromotionCode();
        if (roomPromotionCode != null) {
            totalRefund *= (100 - roomPromotionCode.getDiscountPercent() ) / 100;
        }
        totalRefund *= nights;
        totalRefund *= 0.8;

        // Transfer Message
        String transferMessage = "hotel refund: " + String.format("%.0f",totalRefund) + " (VND). booking code: " + bookingCode.getBookingCode()
                + " .room type: " + bookingDetail.getRoomType().getName() ;

        // Start Transfer
        BankAccountTransfer transfer = new BankAccountTransfer(hotelAccount.getBankAccount(), customerAccount, totalRefund, transferMessage);

        String url = "http://localhost:7777/transfer/";
        RestTemplate restTemplate = new RestTemplate();
        Map<Object, Object> responseMap = restTemplate.postForObject(url,transfer, Map.class);
        assert responseMap != null;
        boolean responseValue = (boolean) responseMap.get("isValid");

        // Handle Response
        Map<Object, Object> data = new LinkedHashMap<>();

        if (responseValue) {
            data.put("roomType", roomType);
            data.put("bookingCode", code);
            data.put("bookingCodeObject", bookingCode);
            data.put("totalRefund", totalRefund);
            data.put("isSuccess", changeBookingCodeStatus(data, 3));
        } else {
            data.put("isSuccess", false);
        }
        return data;
    }

    public long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMilli = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMilli,TimeUnit.MILLISECONDS);
    }

}
