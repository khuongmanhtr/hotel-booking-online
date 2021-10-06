package demo.HotelBooking;

import demo.HotelBooking.entity.Room;
import demo.HotelBooking.helper.ResultPagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.sql.Date;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {
//        StandardPasswordEncoder e1 = new StandardPasswordEncoder();
//        System.out.println(e1.encode("654321"));
//        Pbkdf2PasswordEncoder e1 = new Pbkdf2PasswordEncoder();
//        BCryptPasswordEncoder e2 = new BCryptPasswordEncoder();
//        String r1 = e1.encode("123456");
//        String r2 = e2.encode("123456");
//        System.out.println(r1);
//        System.out.println(r2);
//        Date date1 = Date.valueOf("2021-09-10");
//        Date date2 = Date.valueOf("2021-09-12");
//        System.out.println(getDateDiff(date1, date2,TimeUnit.DAYS));
//        Date date3 = new Date(date2.getTime() + (2 * 1000 * 60 * 60 * 24));
//        System.out.println(date3);
        PasswordEncoder encoder = new StandardPasswordEncoder();
        boolean isMatched = encoder.matches("123456", "e9edd24d35bc694c7568f0d67e9d697e187c61bc22f8495b57a7d3cdba29f5403687c7a971780080");
        System.out.println("isMatched: " + isMatched);
    }

//    public static String generateNewBookingCode() {
//        String charString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
//        String numString = "0123456789";
//        String chars = "";
//        String nums = "";
//
//        for (int i = 0; i < 5; i++) {
//            int index1 = (int) (Math.random() * (double) charString.length());
//            chars += (charString.charAt(index1));
//
//            int index2 = (int) (Math.random() * (double) numString.length());
//            nums += (numString.charAt(index2));
//        }
//        return chars+nums;
//    }

}
