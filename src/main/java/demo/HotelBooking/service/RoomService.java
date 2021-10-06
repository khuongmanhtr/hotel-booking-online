package demo.HotelBooking.service;

import demo.HotelBooking.entity.Room;
import demo.HotelBooking.entity.RoomType;
import demo.HotelBooking.helper.IRoomCustom;
import demo.HotelBooking.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoomService {
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomTypeService roomTypeService;

    public Room getRoomByName (String name) {
        return roomRepository.findByName(name);
    }

    public List<IRoomCustom> getAllRoomInHotelWithStatusBaseOnCode(String bookingCode) {
        return roomRepository.getAllRoomInHotelWithStatusBaseOnCode(bookingCode);
    }

    public List<IRoomCustom> getAllRoomInHotelWithStatus() {
        return roomRepository.getAllRoomInHotelWithStatus();
    }

    public Map<Object,Object> handleRoomInformation(String roomName) {
        Map<Object, Object> map = new LinkedHashMap<>();
        Room room = roomRepository.findByName(roomName);

        map.put("roomName", room.getName());
        map.put("roomId", room.getId());
        map.put("isActive", room.isActive());

        RoomType roomType = room.getRoomType();
        Map<Object,Object> roomTypeMap = new LinkedHashMap<>();
        roomTypeMap.put("name", roomType.getName());
        roomTypeMap.put("numberOfPeople", roomType.getNumberOfPeople());
        roomTypeMap.put("description", roomType.getDescription());
        roomTypeMap.put("price", roomType.getPrice());

        map.put("roomType", roomTypeMap);

        return map;
    }

    public List<Map<Object,Object>> getAllRoomInHotel() {
        List<Map<Object,Object>> allRoomList = new ArrayList<>();
        List<Room> allRoom = (List<Room>) roomRepository.findAll();
        for (Room room : allRoom) {
            Map<Object,Object> map = new LinkedHashMap<>();
            map.put("roomId", room.getId());
            map.put("roomName", room.getName());
            allRoomList.add(map);
        }

        return allRoomList;
    }

    public boolean saveRoom(Map<Object, Object> data) {
        int roomId = (int) data.get("roomId");
        String roomName = (String) data.get("roomName");
        String roomTypeString = (String) data.get("roomType");
        boolean roomStatus = (boolean) data.get("roomStatus");

        RoomType roomType = roomTypeService.getRoomTypeByName(roomTypeString);

        Room room = new Room();
        if (roomId != 0) {
            room.setId(roomId);
        }
        room.setName(roomName);
        room.setRoomType(roomType);
        room.setActive(roomStatus);

        roomRepository.save(room);
        return true;
    }

    public boolean deleteRoom(int id) {
        roomRepository.deleteById(id);
        return true;
    }
}
