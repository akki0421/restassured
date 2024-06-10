package Utility;

import java.util.Random;

public class Generator {
    public static String registration_no() {
        Random rnd = new Random();
        int four_digit = rnd.nextInt(9999);
        String newNumber_fourDigit = String.format("%04d", four_digit);
        int two_digit = rnd.nextInt(99);
        String newNumber_twoDigit = String.format("%02d", two_digit);
        char S = (char) (rnd.nextInt(26) + 'A');
        return "KA" + newNumber_twoDigit + S + newNumber_fourDigit;
    }

    public static String transaction_id() {
        Random rnd = new Random();
        int six_digit = rnd.nextInt(999999);
        return String.format("%04d", six_digit);
    }
}
