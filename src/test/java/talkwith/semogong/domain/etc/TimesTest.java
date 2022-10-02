package talkwith.semogong.domain.etc;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TimesTest {

    @Test // 시간 계산 Test (1. END or Breaking 상태의 post 시간계산)
    public void getTimesTest1() throws Exception {
        //given (주어진 것들을 통해)
        List<String> times = new ArrayList<>(List.of("10:00", "12:10", "14:30", "17:20", "18:00", "04:10"));
        //when (이런 기능을 동작했을 때)
        Times result = Times.getTimes(times);
        int total = ((12 * 60 + 10) - (10 * 60)) + ((17 * 60 + 20) - (14 * 60 + 30)) + (((24 + 4) * 60 + 10) - (18 * 60));
        //then (이런 결과를 확인할 것)
        assertThat(result).isEqualTo(new Times(total));
    }

    @Test // 시간 계산 Test (2. Studying 상태의 post 시간계산)
    public void getTimesTest2() throws Exception {
        //given (주어진 것들을 통해)
        List<String> times = new ArrayList<>(List.of("08:00", "09:10", "09:40"));
        String to_add = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        String[] nowTime = to_add.split(":");
        int hour = Integer.parseInt(nowTime[0]);
        int min = Integer.parseInt(nowTime[1]);
        if (0 <= hour & hour < 5) hour += 24;
        int now = hour * 60 + min;
        //when (이런 기능을 동작했을 때)
        Times result = Times.getTimes(times);
        int total = ((9 * 60 + 10) - (8 * 60)) + (now - (9 * 60 + 40));
        //then (이런 결과를 확인할 것)
        assertThat(result).isEqualTo(new Times(total));
    }
}