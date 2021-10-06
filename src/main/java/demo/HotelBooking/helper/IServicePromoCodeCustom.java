package demo.HotelBooking.helper;

public interface IServicePromoCodeCustom {
    int getServicePromoId();
    String getServicePromoCode();
    String getDescription();
    byte getIsUsed();
    String getCurrentStatus();
}
