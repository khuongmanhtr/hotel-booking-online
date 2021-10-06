package demo.HotelBooking.service;

import demo.HotelBooking.entity.*;
import demo.HotelBooking.helper.IRoomPromoCodeCustom;
import demo.HotelBooking.helper.ResultPagination;
import demo.HotelBooking.repository.RoomPromotionCodeRepository;
import demo.HotelBooking.repository.ServicePromotionCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomPromotionCodeService {
    @Autowired
    RoomPromotionCodeRepository roomPromotionCodeRepository;

    @Autowired
    ServicePromotionCodeRepository servicePromotionCodeRepository;

    public RoomPromotionCode getRoomCodeByCode (String code) {
        return roomPromotionCodeRepository.findByCode(code);
    }

    public void saveData (RoomPromotionCode roomPromotionCode) {
        roomPromotionCodeRepository.save(roomPromotionCode);
    }

    public Map<Object, Object> getServiceCodeOrRoomCode(String code) {
        Map<Object, Object> map = new LinkedHashMap<>();
        Map<Object, Object> contents = new LinkedHashMap<>();
        RoomPromotionCode roomCode = roomPromotionCodeRepository.findByCodeAndIsUsed(code, false);
        ServicePromotionCode serviceCode = servicePromotionCodeRepository.findByCodeAndIsUsed(code, false);
        if (roomCode != null) {
            contents.put("all room", roomCode.getDiscountPercent());
            map.put("contents", contents);
            map.put("type", "roomCode");
            map.put("isExisted", true);
        } else {
            if (serviceCode != null) {
                for (ServicePromotionDetail serviceDetail : serviceCode.getServicePromotionDetailList()) {
                    contents.put(serviceDetail.getServiceEntity().getName(), serviceDetail.getDiscountPercent());
                    map.put("contents", contents);
                    map.put("type", "serviceCode");
                    map.put("isExisted", true);
                }
            } else {
                map.put("isExisted", false);
            }
        }
        return map;
    }

    public Map<Object, Object> handleAllRoomPromoCode(int pageIndex, String filter, String searchValue) {
        Map<Object, Object> map = new LinkedHashMap<>();
        List<IRoomPromoCodeCustom> promoCodeList = new ArrayList<>();
        List<Integer> paginationList = new ArrayList<>();
        int sizePerPage = 15;
        int rangePage = 5;
        int minPage = 1;
        int maxPage = 0;
        int resultQuantity = 0;
        int indexOfPageIndex = 0;

        Map<String,String> options = new LinkedHashMap<>();
        options.put("all", "All");
        options.put("promoCode", "Promotion Code");

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
                case "promoCode":
                    resultQuantity = roomPromotionCodeRepository.countAllPromoCodeByCode(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    promoCodeList = roomPromotionCodeRepository.getAllPromoCodeByCode(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
            }
        } else {
            resultQuantity = roomPromotionCodeRepository.countAllPromoCode();
            maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
            paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
            indexOfPageIndex = paginationList.indexOf(pageIndex);
            promoCodeList = roomPromotionCodeRepository.getAllPromoCode(PageRequest.of(pageIndex - 1, sizePerPage));
        }

        map.put("promoCodeList", promoCodeList);
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

    public void saveRoomPromoCode(Map<Object, Object> data) {
        int promoId = (int) data.get("id");
        RoomPromotionCode roomPromotionCode;
        if (promoId == 0) {
            roomPromotionCode = new RoomPromotionCode();
            roomPromotionCode.setUsed(false);
        } else {
            roomPromotionCode = roomPromotionCodeRepository.findById(promoId);
        }

        String code = (String) data.get("code");
        double discount = Double.parseDouble((String) data.get("discount")) ;
        String description = (String) data.get("description");

        roomPromotionCode.setCode(code);
        roomPromotionCode.setDiscountPercent(discount);
        roomPromotionCode.setDescription(description);

        roomPromotionCodeRepository.save(roomPromotionCode);
    }

    public void deleteRoomPromoCode (int id) {
        roomPromotionCodeRepository.deleteById(id);
    }

    public double applyDiscountToPayment(String type, String code, double totalPayment) {
        if (type.equals("roomCode")) {
            RoomPromotionCode roomPromoCode = roomPromotionCodeRepository.findByCode(code);
            if (roomPromoCode != null) {
                totalPayment = (long) (totalPayment * ((100 - roomPromoCode.getDiscountPercent()) / 100));
            }
        }
        return  totalPayment;
    }
}
