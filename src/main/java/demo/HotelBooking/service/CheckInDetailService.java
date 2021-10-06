package demo.HotelBooking.service;

import demo.HotelBooking.entity.*;
import demo.HotelBooking.helper.ResultPagination;
import demo.HotelBooking.repository.CheckInDetailRepository;
import demo.HotelBooking.repository.ServiceEntityRepository;
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
public class CheckInDetailService {
    @Autowired
    CheckInDetailRepository checkInDetailRepository;

    @Autowired
    ServiceEntityRepository serviceEntityRepository;

    @Autowired
    RoomService roomService;

    @Autowired
    BookingCodeService bookingCodeService;

    public Map<Object, Object> handleAllActiveRoomBy (int pageIndex, String filter, String searchValue) {
        Map<Object, Object> map = new LinkedHashMap<>();
        List<CheckInDetail> checkInDetailList = new ArrayList<>();
        List<Integer> paginationList = new ArrayList<>();
        int sizePerPage = 15;
        int rangePage = 5;
        int minPage = 1;
        int maxPage = 0;
        int resultQuantity = 0;
        int indexOfPageIndex = 0;


        Map<String,String> options = new LinkedHashMap<>();
        options.put("all", "All");
        options.put("room", "Room");
        options.put("bookingCode", "Booking Code");

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
                case "room":
                    resultQuantity = checkInDetailRepository.countAllActiveRoomsByRoom(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    checkInDetailList = checkInDetailRepository.getAllActiveRoomsByRoom(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
                case "bookingCode":
                    resultQuantity = checkInDetailRepository.countAllActiveRoomsByBookingCode(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    checkInDetailList = checkInDetailRepository.getAllActiveRoomsByBookingCode(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
            }
        } else {
            resultQuantity = checkInDetailRepository.countAllActiveRooms();
            maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
            paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
            indexOfPageIndex = paginationList.indexOf(pageIndex);
            checkInDetailList = checkInDetailRepository.getAllActiveRooms(PageRequest.of(pageIndex - 1, sizePerPage));
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

        map.put("resultQuantity", resultQuantity);
        map.put("maxPage", maxPage);
        map.put("minPage", minPage);
        map.put("pageIndex", pageIndex);
        map.put("paginationList", paginationList);
        map.put("indexOfPageIndex", indexOfPageIndex);
        map.put("checkInDetailList", checkInDetailList);
        map.put("serviceDetailInPerRoom", serviceDetailInPerRoom);
        map.put("options", options);
        map.put("filter", filter);
        map.put("searchValue", searchValue);

        return map;
    }

    public Map<Object, Object> getRoomByRoomAndCode (Map<String, String> data) {
        Map<Object, Object> map = new LinkedHashMap<>();
        String room = data.get("room");
        String bookingCode = data.get("code");

        Date today = Date.valueOf(LocalDate.now());

        CheckInDetail checkInDetail =  checkInDetailRepository.getRoomByRoomAndCode(room, bookingCode);

        if (checkInDetail != null) {
            List<Map<Object, Object>> serviceInRoom = new ArrayList<>();
            List<String> serviceList = new ArrayList<>();

            for (ServiceEntity service : serviceEntityRepository.findAll()) {
                serviceList.add(service.getName());
            }

            String roomNameCID = checkInDetail.getRoom().getName();

            for (ServicePerRoom s : checkInDetail.getBookingCode().getServicePerRoomList()) {
                String roomNameSPR = s.getRoom().getName();
                if (roomNameCID.equals(roomNameSPR)) {
                    Map<Object, Object> serviceMap = new LinkedHashMap<>();
                    serviceMap.put("serviceName",s.getServiceEntity().getName());
                    serviceMap.put("dateRegistry",s.getRegistryDate());
                    serviceInRoom.add(serviceMap);
                }
            }

            map.put("today", today);
            map.put("room", checkInDetail.getRoom().getName());
            map.put("code", checkInDetail.getBookingCode().getBookingCode());
            map.put("maxPeople", checkInDetail.getRoom().getRoomType().getNumberOfPeople());
            map.put("checkIn", checkInDetail.getBookingCode().getCheckInDate());
            map.put("peopleInStaying", checkInDetail.getActualPeople());
            map.put("totalService",  serviceInRoom.size());
            map.put("serviceInRoom", serviceInRoom);
            map.put("serviceList", serviceList);
        } else {
            map = null;
        }
        return map;
    }

    public boolean checkIn(Map<Object, Object> data) {
        String code = (String) data.get("bookingCode");

        BookingCode bookingCode = bookingCodeService.getBookingCodeByCode(code.trim());
        if (bookingCode == null) {
            return false;
        }

        Date today = Date.valueOf(LocalDate.now());
        bookingCode.setCheckInDate(today);

        List<Map<Object,Object>> rooms = (List<Map<Object, Object>>) data.get("rooms");

        for (Map<Object, Object> m : rooms) {
            CheckInDetail checkInDetail = new CheckInDetail();

            String roomName = (String) m.get("room");
            Room room = roomService.getRoomByName(roomName);
            if (room == null) {
                return false;
            }
            int actualPeople = Integer.parseInt((String) m.get("actualPeople"));

            checkInDetail.setBookingCode(bookingCode);
            checkInDetail.setRoom(room);
            checkInDetail.setActualPeople(actualPeople);
            checkInDetailRepository.save(checkInDetail);
        }
        return true;
    }


}
