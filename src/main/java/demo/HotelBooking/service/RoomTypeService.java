package demo.HotelBooking.service;

import demo.HotelBooking.entity.RoomType;
import demo.HotelBooking.helper.IRoomTypeCustom;
import demo.HotelBooking.helper.IRoomTypeStatus;
import demo.HotelBooking.helper.ResultPagination;
import demo.HotelBooking.repository.RoomTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RoomTypeService {
    @Autowired
    RoomTypeRepository roomTypeRepository;

    @Autowired
    BookingCodeService bookingCodeService;

    public List<RoomType> getAllRoomType() {
        return (List<RoomType>) roomTypeRepository.findAll();
    }

    public RoomType getRoomTypeByName(String roomTypeName) {
        return roomTypeRepository.findByName(roomTypeName);
    }

    public RoomType getRoomTypeById(int roomTypeId) {
        return roomTypeRepository.findById(roomTypeId);
    }

    public List<RoomType> getAvailableRoomTypes (Date dateFromCheck, Date dateToCheck) {
        List<RoomType> availableRoomTypes = new ArrayList<>();
        long dateDiff = getDateDiff(dateFromCheck, dateToCheck, TimeUnit.DAYS);

        List<RoomType> roomTypes = (List<RoomType>) roomTypeRepository.findAll();
        for (RoomType roomType : roomTypes) {
            List<Long> totalRoomPerDayPerRoomType = new ArrayList<>();

            for (int i = 0; i < dateDiff; i++) {
                Date dateCheck = dateAfterPlusBy(dateFromCheck, i);
                long totalRoom = roomTypeRepository.getTotalRoomPerDay(dateCheck, dateAfterPlusBy(dateCheck, 1),roomType.getName());
                totalRoomPerDayPerRoomType.add(totalRoom);
            }
            long maximumTotal = Collections.max(totalRoomPerDayPerRoomType);
            long totalRoomPerRoomType = roomTypeRepository.getTotalRoomByRoomType(roomType.getName());
            long availableRoom = totalRoomPerRoomType - maximumTotal;

            if (availableRoom > 0) {
                availableRoomTypes.add(roomType);
            }
        }
        return availableRoomTypes;
    }

    // test List subtitle for String[]
    public Map<RoomType, Long> getRoomTypeMap (List<String> roomTypeNames, Date dateFromCheck, Date dateToCheck) {
        Map<RoomType, Long> roomTypeMap = new LinkedHashMap<>();
        long dateDiff = getDateDiff(dateFromCheck, dateToCheck, TimeUnit.DAYS);

        for (int i = 0; i < roomTypeNames.size(); i++) {
            RoomType roomType = roomTypeRepository.findByName(roomTypeNames.get(i));

            List<Long> totalRoomPerDayPerRoomType = new ArrayList<>();

            for (int j = 0; j < dateDiff; j++) {
                Date dateCheck = dateAfterPlusBy(dateFromCheck, j);
                long totalRoom = roomTypeRepository.getTotalRoomPerDay(dateCheck,dateAfterPlusBy(dateCheck, 1),roomType.getName());
                totalRoomPerDayPerRoomType.add(totalRoom);
            }

            long maximumTotal = Collections.max(totalRoomPerDayPerRoomType);
            long totalRoomPerRoomType = roomTypeRepository.getTotalRoomByRoomType(roomType.getName());
            long availableRoom = totalRoomPerRoomType - maximumTotal;
            roomTypeMap.put(roomType, availableRoom);
        }
        return roomTypeMap;
    }

    public Map<Object, Object> handleAllRoomTypeParameter() {
        List<RoomType> roomTypeList = getAllRoomType();
        List<IRoomTypeCustom> allTotalPerRoomType = roomTypeRepository.getAllTotalRoomPerRoomType();
        List<IRoomTypeCustom> allTotalBeingUsedPerRoomType = roomTypeRepository.getTotalBeingUsedPerRoomType();
        Date today = Date.valueOf(LocalDate.now());
        Date maxDateTo = bookingCodeService.getMaxDateTo();

        long dateDiff = 0;
        if (maxDateTo != null) {
            dateDiff = getDateDiff(today, maxDateTo, TimeUnit.DAYS);
        }

        Map<Object, Object> roomTypeMap = new LinkedHashMap<>();

        for (RoomType roomType : roomTypeList) {
            Map<String, Object> map = new LinkedHashMap<>();
            String roomTypeName = roomType.getName();

            for (IRoomTypeCustom iRoomType : allTotalPerRoomType) {
                String iRoomTypeName = iRoomType.getTypeName();
                if (roomTypeName.equals(iRoomTypeName)) {
                    map.put("totalQuantity", iRoomType.getParameter());
                    break;
                }
            }

            for (IRoomTypeCustom iRoomType : allTotalBeingUsedPerRoomType) {
                String iRoomTypeName = iRoomType.getTypeName();
                if (roomTypeName.equals(iRoomTypeName)) {
                    map.put("beingUsed", iRoomType.getParameter());
                    break;
                }
            }

            List<Long> totalRoomListPerRoomType = new ArrayList<>();
            for (int i = 0; i < dateDiff; i++) {
                Date dateCheck = dateAfterPlusBy(today, i);
                long totalRoom = roomTypeRepository.getTotalRoomPerDay(dateCheck,dateAfterPlusBy(dateCheck, 1),roomType.getName());
                totalRoomListPerRoomType.add(totalRoom);
            }
            long maximumTotal = Collections.max(totalRoomListPerRoomType);
            map.put("maximumUsageCapacity", maximumTotal);

            roomTypeMap.put(roomTypeName, map);
        }

        return roomTypeMap;
    }

    public List<Map<Object,Object>> getAllRoomTypeInformation() {
        List<Map<Object,Object>> allRoomType = new ArrayList<>();

        List<RoomType> roomTypeList = getAllRoomType();

        for (RoomType roomType : roomTypeList) {
            Map<Object,Object> map = new LinkedHashMap<>();
            map.put("roomType", roomType.getName());
            map.put("numberOfPeople", roomType.getNumberOfPeople());
            map.put("price", roomType.getPrice());
            map.put("description", roomType.getDescription());
            allRoomType.add(map);
        }
        return allRoomType;
    }

    public Map<Object, Object> handleAllRoomType(int pageIndex, String filter, String searchValue) {
        Map<Object, Object> map = new LinkedHashMap<>();
        List<IRoomTypeStatus> allRoomTypeWithStatus = new ArrayList<>();
        List<Integer> paginationList = new ArrayList<>();
        int sizePerPage = 15;
        int rangePage = 5;
        int minPage = 1;
        int maxPage = 0;
        int resultQuantity = 0;
        int indexOfPageIndex = 0;

        Map<String,String> options = new LinkedHashMap<>();
        options.put("all", "All");
        options.put("roomName", "Room Name");

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
                case "roomName":
                    resultQuantity = roomTypeRepository.countAllRoomTypeStatusByName(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    allRoomTypeWithStatus = roomTypeRepository.getAllRoomTypeStatusByName(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
            }
        } else {
            resultQuantity = roomTypeRepository.countAllRoomTypeStatus();
            maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
            paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
            indexOfPageIndex = paginationList.indexOf(pageIndex);
            allRoomTypeWithStatus = roomTypeRepository.getAllRoomTypeStatus(PageRequest.of(pageIndex - 1, sizePerPage));
        }

        map.put("resultQuantity", resultQuantity);
        map.put("maxPage", maxPage);
        map.put("minPage", minPage);
        map.put("pageIndex", pageIndex);
        map.put("paginationList", paginationList);
        map.put("indexOfPageIndex", indexOfPageIndex);
        map.put("allRoomType", allRoomTypeWithStatus);
        map.put("options", options);
        map.put("filter", filter);
        map.put("searchValue", searchValue);

        return map;
    }

    public void saveData (int id, String roomTypeName, String numberOfPeople, String price, String description, MultipartFile imageFile) throws IOException {
        RoomType roomType;
        if (id != 0) {
            roomType = roomTypeRepository.findById(id);
        } else {
            roomType = new RoomType();
        }
        roomType.setName(roomTypeName);
        roomType.setNumberOfPeople(Integer.parseInt(numberOfPeople));
        roomType.setPrice(Double.parseDouble(price));
        roomType.setDescription(description);
        if (imageFile != null) {
            String folder = "src/main/resources/static/assets/image/room/";
            byte[] bytes = imageFile.getBytes();

            Path path = Paths.get(folder + imageFile.getOriginalFilename());
            Files.write(path, bytes);

            roomType.setAvatar(imageFile.getOriginalFilename());
        }
        roomTypeRepository.save(roomType);
    }

    public boolean deleteRoomType(int id) {
        roomTypeRepository.deleteById(id);
        return true;
    }

    public double calculateTotalByRoomTypesAndQuantityList(String[] roomTypeNames, long[] quantityList, double total) {
        for (int i = 0; i < roomTypeNames.length; i++) {
            RoomType roomType = roomTypeRepository.findByName(roomTypeNames[i]);
            total += roomType.getPrice() * quantityList[i];
        }
        return total;
    }

    public long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMilli = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMilli,TimeUnit.MILLISECONDS);
    }

    public Date dateAfterPlusBy(Date date,int day) {
        return new Date(date.getTime() + (day * 1000 * 60 * 60 * 24));
    }


}
