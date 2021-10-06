package demo.HotelBooking.service;

import demo.HotelBooking.entity.HotelBankAccount;
import demo.HotelBooking.repository.HotelBankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HotelBankAccountService {
    @Autowired
    HotelBankAccountRepository hotelBankAccountRepository;

    public HotelBankAccount getBankAccountById (int id) {
        return hotelBankAccountRepository.findById(id);
    }
}
