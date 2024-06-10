package Utility;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FutureDate {
    public static String FutureDate_1DaysAdded() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(ft.format(dNow), formatter);
        LocalDate NextDate = localDate.plusDays(3);
        return String.valueOf(NextDate);
    }
}
