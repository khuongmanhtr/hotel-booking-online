package demo.HotelBooking.service;

import demo.HotelBooking.entity.BookingCode;
import demo.HotelBooking.entity.Room;
import demo.HotelBooking.entity.ServiceEntity;
import demo.HotelBooking.entity.ServicePerRoom;
import demo.HotelBooking.helper.IServiceCustom;
import demo.HotelBooking.repository.RoomRepository;
import demo.HotelBooking.repository.ServiceEntityRepository;
import demo.HotelBooking.repository.ServicePerRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Service
public class ServicePerRoomService {
    @Autowired
    ServicePerRoomRepository servicePerRoomRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    BookingCodeService bookingCodeService;

    @Autowired
    ServiceEntityRepository serviceEntityRepository;

    public boolean updateService(Map<Object, Object> data) {
        String roomName = (String) data.get("room");
        Room room = roomRepository.findByName(roomName);
        BookingCode bookingCode = bookingCodeService.getBookingCodeByCode((String) data.get("code"));
        List<Map<Object,Object>> serviceInRoom = (List<Map<Object, Object>>) data.get("serviceInRoom");

        List<ServicePerRoom> serviceInRoomList = room.getServicePerRoomList();

        for (ServicePerRoom s : serviceInRoomList) {
            servicePerRoomRepository.delete(s);
        }

        for (Map<Object,Object> serviceDetail : serviceInRoom) {
            ServiceEntity serviceEntity = serviceEntityRepository.findByName((String) serviceDetail.get("serviceName"));
            ServicePerRoom servicePerRoom = new ServicePerRoom();
            servicePerRoom.setRoom(room);
            servicePerRoom.setRegistryDate(Date.valueOf((String) serviceDetail.get("dateRegistry")));
            servicePerRoom.setBookingCode(bookingCode);
            servicePerRoom.setServiceEntity(serviceEntity);
            servicePerRoomRepository.save(servicePerRoom);
        }
        return true;
    }

    public List<IServiceCustom> getTotalServiceWithDiscount (String bookingCode, String servicePromoCode) {
        return servicePerRoomRepository.getTotalServiceWithDiscount(bookingCode, servicePromoCode);
    }
}
