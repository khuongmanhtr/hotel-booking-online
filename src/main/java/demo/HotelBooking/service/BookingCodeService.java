package demo.HotelBooking.service;

import demo.HotelBooking.entity.*;
import demo.HotelBooking.helper.IServiceCustom;
import demo.HotelBooking.helper.ResultPagination;
import demo.HotelBooking.repository.BookingCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingCodeService {
    @Autowired
    BookingCodeRepository bookingCodeRepository;

    @Autowired
    ServicePerRoomService servicePerRoomService;

    public String generateBookingCode () {
        String newCode = "";
        do {
            String charString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            String numString = "0123456789";
            String chars = "";
            String nums = "";

            for (int i = 0; i < 5; i++) {
                int index1 = (int) (Math.random() * (double) charString.length());
                chars += (charString.charAt(index1));

                int index2 = (int) (Math.random() * (double) numString.length());
                nums += (numString.charAt(index2));
            }

            newCode = chars + nums;
        } while (bookingCodeRepository.findByBookingCode(newCode) != null);
        return newCode;
    }

    public BookingCode getBookingCodeByCode (String code) {
        return bookingCodeRepository.findByBookingCode(code);
    }

    public Date getMaxDateTo() {
        return bookingCodeRepository.getMaxDateTo();
    }

    public List<BookingCode> getBookingCodesFromDateToDate (Date dateFrom, Date dateTo) {
        return bookingCodeRepository.findBookingCodeByBookingDateBetween(dateFrom,dateTo);
    }

    public BookingCode saveData(String code, String dateFromString, String dateToString) {
        Date bookingDate = Date.valueOf(LocalDate.now());
        Date dateFrom = Date.valueOf(dateFromString);
        Date dateTo = Date.valueOf(dateToString);
        BookingCode bookingCode = new BookingCode();
        bookingCode.setBookingCode(code);
        bookingCode.setBookingDate(bookingDate);
        bookingCode.setDateFrom(dateFrom);
        bookingCode.setDateTo(dateTo);
        bookingCodeRepository.save(bookingCode);
        return bookingCode;
    }

    public Map<Object, Object> handleAllBookingCodeBy (int pageIndex, String filter, String searchValue) {
        Map<Object, Object> map = new LinkedHashMap<>();
        List<BookingCode> bookingCodeList = new ArrayList<>();
        List<Integer> paginationList = new ArrayList<>();
        int sizePerPage = 15;
        int rangePage = 5;
        int minPage = 1;
        int maxPage = 0;
        int resultQuantity = 0;
        int indexOfPageIndex = 0;

        Map<String,String> options = new LinkedHashMap<>();
        options.put("all", "All");
        options.put("bookingCode", "Booking Code");
        options.put("customer", "Customer");
        options.put("bookFrom", "Book From");
        options.put("bookTo", "Book To");

        // Check null
        if (filter == null) {
            filter = "all";
        }

        if (searchValue == null) {
            searchValue = "";
        }
        // Check searchValue is not empty
        if (!searchValue.isEmpty()) {
            searchValue = searchValue.trim();
            switch (filter) {
                case "all":
                    break;
                case "bookingCode":
                    resultQuantity = bookingCodeRepository.countAllBookingCodeByCode(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    bookingCodeList = bookingCodeRepository.getAllBookingCodeByCode(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
                case "customer":
                    resultQuantity = bookingCodeRepository.countAllBookingCodeByCustomer(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    bookingCodeList = bookingCodeRepository.getAllBookingCodeByCustomer(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
                case "bookFrom":
                    Date bookFromDate = Date.valueOf(searchValue);
                    resultQuantity = bookingCodeRepository.countAllBookingCodeByBookFrom(bookFromDate);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    bookingCodeList = bookingCodeRepository.getAllBookingCodeByBookFrom(bookFromDate, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
                case "bookTo":
                    Date bookToDate = Date.valueOf(searchValue);
                    resultQuantity = bookingCodeRepository.countAllBookingCodeByBookTo(bookToDate);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    bookingCodeList = bookingCodeRepository.getAllBookingCodeByBookTo(bookToDate, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
            }
        } else {
            resultQuantity = bookingCodeRepository.countAllBookingCode();
            maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
            paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
            indexOfPageIndex = paginationList.indexOf(pageIndex);
            bookingCodeList = bookingCodeRepository.getAllBookingCode(PageRequest.of(pageIndex - 1, sizePerPage));
        }

        map.put("resultQuantity", resultQuantity);
        map.put("maxPage", maxPage);
        map.put("minPage", minPage);
        map.put("pageIndex", pageIndex);
        map.put("paginationList", paginationList);
        map.put("indexOfPageIndex", indexOfPageIndex);
        map.put("bookingCodeList", bookingCodeList);
        map.put("options", options);
        map.put("filter", filter);
        map.put("searchValue", searchValue);

        return map;
    }

    public Map<Object, Object> getBookingCodeInformation(String code) {
        Map<Object, Object> map = new LinkedHashMap<>();
        BookingCode bookingCode = bookingCodeRepository.findByBookingCode(code);
        if (bookingCode != null) {
            List<Map<Object,Object>> roomTypeList = new ArrayList<>();
            int totalRoom = 0;
            for (BookingDetail bd : bookingCode.getBookingDetailList()) {
                Map<Object, Object> roomTypesMap = new LinkedHashMap<>();
                totalRoom+= bd.getQuantity();
                roomTypesMap.put("roomType", bd.getRoomType().getName());
                roomTypesMap.put("quantity", bd.getQuantity());
                roomTypesMap.put("status", bd.getStatus().getId());
                roomTypesMap.put("statusDesc", bd.getStatus().getDescription());
                roomTypeList.add(roomTypesMap);
            }
            map.put("bookingCode", code);
            map.put("customer", bookingCode.getCustomer().getName());
            map.put("bookFrom", bookingCode.getDateFrom());
            map.put("bookTo", bookingCode.getDateTo());
            map.put("checkIn", bookingCode.getCheckInDate());
            map.put("checkOut", bookingCode.getCheckOutDate());
            map.put("totalRoom", totalRoom);
            map.put("roomTypes", roomTypeList);
        } else {
            map = null;
        }
        return map;
    }

    public Map<Object, Object> handleAllBookingCodeNotCheckIn(int pageIndex, String filter, String searchValue) {
        Map<Object, Object> map = new LinkedHashMap<>();
        List<BookingCode> bookingCodeList = new ArrayList<>();
        List<Integer> paginationList = new ArrayList<>();
        int sizePerPage = 15;
        int rangePage = 5;
        int minPage = 1;
        int maxPage = 0;
        int resultQuantity = 0;
        int indexOfPageIndex = 0;

        Map<String,String> options = new LinkedHashMap<>();
        options.put("all", "All");
        options.put("bookingCode", "Booking Code");
        options.put("customer", "Customer");
        options.put("bookDate", "Book Date");
        options.put("bookFrom", "Book From");
        options.put("bookTo", "Book To");

        // Check null
        if (filter == null) {
            filter = "all";
        }

        if (searchValue == null) {
            searchValue = "";
        }
        // Check searchValue is not empty
        if (!searchValue.isEmpty()) {
            searchValue = searchValue.trim();
            switch (filter) {
                case "all":
                    break;
                case "bookingCode":
                    resultQuantity = bookingCodeRepository.countAllBookingCodeNotCheckInByCode(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    bookingCodeList = bookingCodeRepository.getAllBookingCodeNotCheckInByCode(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
                case "customer":
                    resultQuantity = bookingCodeRepository.countAllBookingCodeNotCheckInByCustomer(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    bookingCodeList = bookingCodeRepository.getAllBookingCodeNotCheckInByCustomer(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
                case "bookDate":
                    Date bookDate = Date.valueOf(searchValue);
                    resultQuantity = bookingCodeRepository.countAllBookingCodeNotCheckInByBookDate(bookDate);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    bookingCodeList = bookingCodeRepository.getAllBookingCodeNotCheckInByBookDate(bookDate, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
                case "bookFrom":
                    Date bookFromDate = Date.valueOf(searchValue);
                    resultQuantity = bookingCodeRepository.countAllBookingCodeNotCheckInByBookFrom(bookFromDate);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    bookingCodeList = bookingCodeRepository.getAllBookingCodeNotCheckInByBookFrom(bookFromDate, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
                case "bookTo":
                    Date bookToDate = Date.valueOf(searchValue);
                    resultQuantity = bookingCodeRepository.countAllBookingCodeNotCheckInByBookTo(bookToDate);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    bookingCodeList = bookingCodeRepository.getAllBookingCodeNotCheckInByBookTo(bookToDate, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
            }
        } else {
            resultQuantity = bookingCodeRepository.countAllBookingCodeNotCheckIn();
            maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
            paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
            indexOfPageIndex = paginationList.indexOf(pageIndex);
            bookingCodeList = bookingCodeRepository.getAllBookingCodeNotCheckIn(PageRequest.of(pageIndex - 1, sizePerPage));
        }

        map.put("bookingCodeList", bookingCodeList);
//        map.put("resultQuantity", resultQuantity);
        map.put("maxPage", maxPage);
        map.put("minPage", minPage);
        map.put("pageIndex", pageIndex);
        map.put("paginationList", paginationList);
        map.put("indexOfPageIndex", indexOfPageIndex);
        map.put("options", options);
        map.put("filter", filter);
        map.put("searchValue", searchValue);

        return map;
    }

    public Map<Object, Object> handleAllBookingCodeCheckIn(int pageIndex, String filter, String searchValue) {
        Map<Object, Object> map = new LinkedHashMap<>();
        List<BookingCode> bookingCodeList = new ArrayList<>();
        List<Integer> paginationList = new ArrayList<>();
        int sizePerPage = 15;
        int rangePage = 5;
        int minPage = 1;
        int maxPage = 0;
        int resultQuantity = 0;
        int indexOfPageIndex = 0;

        Map<String,String> options = new LinkedHashMap<>();
        options.put("all", "All");
        options.put("bookingCode", "Booking Code");
        options.put("customer", "Customer");
        options.put("bookDate", "Book Date");
        options.put("bookFrom", "Book From");
        options.put("bookTo", "Book To");

        // Check null
        if (filter == null) {
            filter = "all";
        }

        if (searchValue == null) {
            searchValue = "";
        }
        // Check searchValue is not empty
        if (!searchValue.isEmpty()) {
            searchValue = searchValue.trim();
            switch (filter) {
                case "all":
                    break;
                case "bookingCode":
                    resultQuantity = bookingCodeRepository.countAllBookingCodeCheckInByCode(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    bookingCodeList = bookingCodeRepository.getAllBookingCodeCheckInByCode(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
                case "customer":
                    resultQuantity = bookingCodeRepository.countAllBookingCodeCheckInByCustomer(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    bookingCodeList = bookingCodeRepository.getAllBookingCodeCheckInByCustomer(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
                case "bookDate":
                    Date bookDate = Date.valueOf(searchValue);
                    resultQuantity = bookingCodeRepository.countAllBookingCodeCheckInByBookDate(bookDate);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    bookingCodeList = bookingCodeRepository.getAllBookingCodeCheckInByBookDate(bookDate, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
                case "bookFrom":
                    Date bookFromDate = Date.valueOf(searchValue);
                    resultQuantity = bookingCodeRepository.countAllBookingCodeCheckInByBookFrom(bookFromDate);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    bookingCodeList = bookingCodeRepository.getAllBookingCodeCheckInByBookFrom(bookFromDate, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
                case "bookTo":
                    Date bookToDate = Date.valueOf(searchValue);
                    resultQuantity = bookingCodeRepository.countAllBookingCodeCheckInByBookTo(bookToDate);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    bookingCodeList = bookingCodeRepository.getAllBookingCodeCheckInByBookTo(bookToDate, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
            }
        } else {
            resultQuantity = bookingCodeRepository.countAllBookingCodeCheckIn();
            maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
            paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
            indexOfPageIndex = paginationList.indexOf(pageIndex);
            bookingCodeList = bookingCodeRepository.getAllBookingCodeCheckIn(PageRequest.of(pageIndex - 1, sizePerPage));
        }

        map.put("bookingCodeList", bookingCodeList);
//        map.put("resultQuantity", resultQuantity);
        map.put("maxPage", maxPage);
        map.put("minPage", minPage);
        map.put("pageIndex", pageIndex);
        map.put("paginationList", paginationList);
        map.put("indexOfPageIndex", indexOfPageIndex);
        map.put("options", options);
        map.put("filter", filter);
        map.put("searchValue", searchValue);

        return map;
    }

    public Map<Object, Object> getAllInformationToCheckOut (String code) {
        Map<Object, Object> map = new LinkedHashMap<>();
        BookingCode bookingCode = bookingCodeRepository.findByBookingCode(code);
        List<CheckInDetail> checkInDetailList = bookingCode.getCheckInDetailList();

        RoomPromotionCode roomPromotionCode = bookingCode.getCustomer().getRoomPromotionCode();
        ServicePromotionCode servicePromotionCode = bookingCode.getCustomer().getServicePromotionCode();
        String serviceCode = "";
        if (roomPromotionCode != null) {
            map.put("promoCode", roomPromotionCode);
            map.put("promoType", "Room Discount");
        } else if (servicePromotionCode != null) {
            serviceCode = servicePromotionCode.getCode();
            map.put("promoCode", servicePromotionCode);
            map.put("promoType", "Service Discount");
        } else {
            map.put("promoCode", null);
            map.put("promoType", null);
        }

        Map<String, List<ServicePerRoom>> serviceDetailInPerRoom = new LinkedHashMap<>();
        for (CheckInDetail cid : checkInDetailList) {
            String roomNameCID = cid.getRoom().getName();
            List<ServicePerRoom> servicePerRoomList = new ArrayList<>();

            for (ServicePerRoom spr : cid.getBookingCode().getServicePerRoomList()) {
                String roomNameSPR = spr.getRoom().getName();
                if (roomNameCID.equals(roomNameSPR)) {
                    servicePerRoomList.add(spr);
                }
            }
            serviceDetailInPerRoom.put(roomNameCID, servicePerRoomList);
        }

        List<IServiceCustom> allTotalService = servicePerRoomService.getTotalServiceWithDiscount(code, serviceCode);

        map.put("bookingCode", bookingCode);
        map.put("checkInDetailList", checkInDetailList);
        map.put("serviceDetailInPerRoom", serviceDetailInPerRoom);
        map.put("allTotalService", allTotalService);
        return map;
    }

    public boolean checkOut(String code) {
        BookingCode bookingCode = bookingCodeRepository.findByBookingCode(code);
        if (bookingCode != null) {
            Date today = Date.valueOf(LocalDate.now());
            bookingCode.setCheckOutDate(today);
            bookingCodeRepository.save(bookingCode);
            return true;
        } else {
            return false;
        }
    }

}
