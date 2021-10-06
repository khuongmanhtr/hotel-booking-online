package demo.HotelBooking.service;

import demo.HotelBooking.entity.BookingCode;
import demo.HotelBooking.entity.Customer;
import demo.HotelBooking.entity.RoomPromotionCode;
import demo.HotelBooking.entity.ServicePromotionCode;
import demo.HotelBooking.helper.ResultPagination;
import demo.HotelBooking.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RoomPromotionCodeService roomPromotionCodeService;

    @Autowired
    ServicePromotionCodeService servicePromotionCodeService;

    public void saveData(BookingCode bookingCode, String customerName,
                         String customerAccount, double totalPayment,
                         String promoCode, String promoType) {
        Customer customer = new Customer();
        if (promoType.equals("roomCode")) {
            RoomPromotionCode roomCode = roomPromotionCodeService.getRoomCodeByCode(promoCode);
            roomCode.setUsed(true);
            roomPromotionCodeService.saveData(roomCode);
            customer.setRoomPromotionCode(roomCode);
        } else if (promoType.equals("serviceCode")) {
            ServicePromotionCode serviceCode = servicePromotionCodeService.getServiceCodeByCode(promoCode);
            serviceCode.setUsed(true);
            servicePromotionCodeService.saveData(serviceCode);
            customer.setServicePromotionCode(serviceCode);
        }
        customer.setBookingCodeDetail(bookingCode);
        customer.setBookingCode(bookingCode.getBookingCode());
        customer.setAdvancePayment(totalPayment);
        customer.setBankAccount(customerAccount);
        customer.setName(customerName);
        customerRepository.save(customer);
    }

    public Map<Object, Object> handleAllCustomerBy (int pageIndex, String filter, String searchValue) {
        Map<Object, Object> map = new LinkedHashMap<>();
        List<Customer> customerList = new ArrayList<>();
        List<Integer> paginationList = new ArrayList<>();
        int sizePerPage = 15;
        int rangePage = 5;
        int minPage = 1;
        int maxPage = 0;
        int resultQuantity = 0;
        int indexOfPageIndex = 0;

        Map<String,String> options = new LinkedHashMap<>();
        options.put("all", "All");
        options.put("customer", "Customer");
        options.put("bookingCode", "Booking Code");
        options.put("roomPromo", "Room Promo Code");
        options.put("servicePromo", "Service Promo Code");
        options.put("bankAccount", "Bank Account");

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
                case "customer":
                    resultQuantity = customerRepository.countAllCustomerByName(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    customerList = customerRepository.getAllCustomerByName(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
                case "bookingCode":
                    resultQuantity = customerRepository.countAllCustomerByCode(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    customerList = customerRepository.getAllCustomerByCode(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
                case "roomPromo":
                    resultQuantity = customerRepository.countAllCustomerByRoomPromo(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    customerList = customerRepository.getAllCustomerByRoomPromo(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
                case "servicePromo":
                    resultQuantity = customerRepository.countAllCustomerByServicePromo(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    customerList = customerRepository.getAllCustomerByServicePromo(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
                case "bankAccount":
                    resultQuantity = customerRepository.countAllCustomerByBankAccount(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    customerList = customerRepository.getAllCustomerByBankAccount(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
            }
        } else {
            resultQuantity = customerRepository.countAllCustomer();
            maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
            paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
            indexOfPageIndex = paginationList.indexOf(pageIndex);
            customerList = customerRepository.getAllCustomer(PageRequest.of(pageIndex - 1, sizePerPage));
        }

        map.put("resultQuantity", resultQuantity);
        map.put("maxPage", maxPage);
        map.put("minPage", minPage);
        map.put("pageIndex", pageIndex);
        map.put("paginationList", paginationList);
        map.put("indexOfPageIndex", indexOfPageIndex);
        map.put("customerList", customerList);
        map.put("options", options);
        map.put("filter", filter);
        map.put("searchValue", searchValue);

        return map;
    }
}
