package talkwith.semogong.domain.etc;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class CustomLocalDate {
    private LocalDate customDate;

    public static LocalDate now() {
        CustomLocalDate customNow = new CustomLocalDate();
        int hour = LocalDateTime.now().getHour();
        if (hour < 5) { // yesterday
            customNow.customDate = LocalDate.now().minusDays(1);
        } else { // today
            customNow.customDate = LocalDate.now();
        }
        return customNow.customDate;
    }
}
