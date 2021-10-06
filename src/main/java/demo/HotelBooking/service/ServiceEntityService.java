package demo.HotelBooking.service;

import demo.HotelBooking.entity.BookingCode;
import demo.HotelBooking.entity.ServiceEntity;
import demo.HotelBooking.entity.ServicePromotionDetail;
import demo.HotelBooking.helper.IServiceEntityCustom;
import demo.HotelBooking.helper.ResultPagination;
import demo.HotelBooking.repository.ServiceEntityRepository;
import demo.HotelBooking.repository.ServicePromotionDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceEntityService {
    @Autowired
    ServiceEntityRepository serviceEntityRepository;

    @Autowired
    ServicePromotionDetailRepository servicePromotionDetailRepository;

    public ServiceEntity getServiceEntityById(int id) {
        return serviceEntityRepository.findById(id);
    }

    public Map<Object, Object> handAllService(int pageIndex, String filter, String searchValue) {
        Map<Object, Object> map = new LinkedHashMap<>();
        List<IServiceEntityCustom> serviceList = new ArrayList<>();
        List<Integer> paginationList = new ArrayList<>();
        int sizePerPage = 15;
        int rangePage = 5;
        int minPage = 1;
        int maxPage = 0;
        int resultQuantity = 0;
        int indexOfPageIndex = 0;

        Map<String,String> options = new LinkedHashMap<>();
        options.put("all", "All");
        options.put("serviceName", "Service Name");

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
                case "serviceName":
                    resultQuantity = serviceEntityRepository.countAllServiceByName(searchValue);
                    maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
                    paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
                    indexOfPageIndex = paginationList.indexOf(pageIndex);
                    serviceList = serviceEntityRepository.getAllServiceByName(searchValue, PageRequest.of(pageIndex - 1, sizePerPage));
                    break;
            }
        } else {
            resultQuantity = serviceEntityRepository.countAllService();
            maxPage = ResultPagination.calculateMaxCountPage(resultQuantity, sizePerPage);
            paginationList = ResultPagination.getPaginationList(minPage, maxPage, pageIndex, rangePage);
            indexOfPageIndex = paginationList.indexOf(pageIndex);
            serviceList = serviceEntityRepository.getAllService(PageRequest.of(pageIndex - 1, sizePerPage));
        }

        map.put("serviceList", serviceList);
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

    public List<Map<Object, Object>> getAllService() {
        List<Map<Object, Object>> serviceList = new ArrayList<>();

        List<ServiceEntity> serviceEntityList = (List<ServiceEntity>) serviceEntityRepository.findAll();
        for (ServiceEntity se : serviceEntityList) {
            Map<Object, Object> map = new LinkedHashMap<>();
            map.put("id", se.getId());
            map.put("name", se.getName());
            map.put("price", se.getPrice());
            map.put("description", se.getDescription());
            serviceList.add(map);
        }

        return serviceList;
    }

    public void saveService (Map<Object, Object> data) {
        int serviceId = (int) data.get("id");
        ServiceEntity serviceEntity;
        if (serviceId == 0) {
            serviceEntity = new ServiceEntity();
        } else {
            serviceEntity = serviceEntityRepository.findById(serviceId);
        }

        String serviceName = (String) data.get("name");
        double price = Double.parseDouble((String) data.get("price")) ;
        String description = (String) data.get("description");
        serviceEntity.setName(serviceName);
        serviceEntity.setPrice(price);
        serviceEntity.setDescription(description);

        serviceEntityRepository.save(serviceEntity);
    }

    public void deleteService(int id) {
        List<ServicePromotionDetail> servicePromotionDetailList = servicePromotionDetailRepository.getServicePromotionDetailListByServiceId(id);
        for (ServicePromotionDetail spd : servicePromotionDetailList) {
            servicePromotionDetailRepository.delete(spd);
        }
        serviceEntityRepository.deleteById(id);
    }
}
