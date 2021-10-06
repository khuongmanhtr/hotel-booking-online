package demo.HotelBooking.helper;

public interface IRoomPromoCodeCustom {
    int getRoomPromoId();
    String getRoomPromoCode();
    String getDescription();
    double getDiscount();
    byte getIsUsed();
    String getCurrentStatus();
}
