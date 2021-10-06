package demo.HotelBooking.service;

import demo.HotelBooking.entity.ServiceEntity;
import demo.HotelBooking.entity.ServicePromotionCode;
import demo.HotelBooking.entity.ServicePromotionDetail;
import demo.HotelBooking.helper.IServicePromoCodeCustom;
import demo.HotelBooking.helper.ResultPagination;
import demo.HotelBooking.repository.ServiceEntityRepository;
import demo.HotelBooking.repository.ServicePromotionCodeRepository;
import demo.HotelBooking.repository.ServicePromotionDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServicePromotionCodeService {
    @Autowired
    ServicePromotionCodeRepository servicePromotionCodeRepository;

   @Autowired
    ServicePromotionDetailRepository servicePromotionDetailRepository;

    @Autowired
    ServiceEntityService serviceEntityService;

    public ServicePromotionCode getServiceCodeByCode(String code) {
        return servicePromotionCodeRepository.findByCode(code);
    }

    public void saveData (ServicePromotionCode servicePromotionCode) {
        servicePromotionCodeRepository.save(servicePromotionCode);
    }

    public Map<Object, Object> handleAllServicePromoCode(int pageIndex, String filter, String searchValue) {
        Map<Object, Object> map = new LinkedHashMap<>();
        List<IServicePromoCodeCustom> promoCodeList = new ArrayList<>();
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
                    resultQuantity = servicePromotionCodeRepository.countAllPromoCodeByCode(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    promoCodeList = servicePromotionCodeRepository.getAllPromoCodeByCode(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
            }
        } else {
            resultQuantity = servicePromotionCodeRepository.countAllPromoCode();
            maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
            paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
            indexOfPageIndex = paginationList.indexOf(pageIndex);
            promoCodeList = servicePromotionCodeRepository.getAllPromoCode(PageRequest.of(pageIndex - 1, sizePerPage));
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

    public List<Map<Object, Object>> getServicePromoDetailList(int id) {
        List<Map<Object, Object>> servicePromoDetailList = new ArrayList<>();
        ServicePromotionCode promoCode = servicePromotionCodeRepository.findById(id);
        for (ServicePromotionDetail spd : promoCode.getServicePromotionDetailList()) {
            Map<Object, Object> map = new LinkedHashMap<>();
            map.put("serviceId", spd.getServiceEntity().getId());
            map.put("serviceName", spd.getServiceEntity().getName());
            map.put("discount", String.valueOf(spd.getDiscountPercent()));
            servicePromoDetailList.add(map);
        }
        return servicePromoDetailList;
    }

    public void saveServicePromoCode(Map<Object, Object> data) {
        int promoId = (int) data.get("promoId");
        ServicePromotionCode servicePromotionCode;
        if (promoId == 0) {
            servicePromotionCode = new ServicePromotionCode();
            servicePromotionCode.setUsed(false);
        } else {
            servicePromotionCode = servicePromotionCodeRepository.findById(promoId);
        }

        String promoCode = (String) data.get("promoCode");
        String description = (String) data.get("promoDesc");
        servicePromotionCode.setCode(promoCode);
        servicePromotionCode.setDescription(description);
        servicePromotionCodeRepository.save(servicePromotionCode);

        List<ServicePromotionDetail> servicePromotionDetailList = servicePromotionCode.getServicePromotionDetailList();
        if (servicePromotionDetailList != null) {
            for (ServicePromotionDetail spd : servicePromotionDetailList) {
                servicePromotionDetailRepository.delete(spd);
            }
        }

        List<Map<Object, Object>> serviceDetails = (List<Map<Object, Object>>) data.get("serviceDetails");
        for (Map<Object,Object> serviceDetail : serviceDetails) {
            ServicePromotionDetail spd = new ServicePromotionDetail();

            int serviceId = (int) serviceDetail.get("serviceId");
            ServiceEntity serviceEntity = serviceEntityService.getServiceEntityById(serviceId);
            double discount = Double.parseDouble((String) serviceDetail.get("discount"));

            spd.setServiceEntity(serviceEntity);
            spd.setDiscountPercent(discount);
            spd.setServicePromotionCode(servicePromotionCode);
            servicePromotionDetailRepository.save(spd);
        }
    }

    public void deleteServicePromoCode(int id) {
        List<ServicePromotionDetail> servicePromotionDetailList = servicePromotionDetailRepository.getServicePromotionDetailListByPromoId(id);
        for (ServicePromotionDetail spd : servicePromotionDetailList) {
            servicePromotionDetailRepository.delete(spd);
        }
        servicePromotionCodeRepository.deleteById(id);
    }
}
