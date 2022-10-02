package talkwith.semogong.domain.etc;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Times {

    private int hour;
    private int min;

    public Times(int total) {
        this.hour = total / 60;
        this.min = total % 60;
    }

    // 한 Post의 공부시간 구하기 (오늘 이전의 post들은 모두 End인 상태라고 가정 -> 5시 자동 종료)
    public static Times getTimes(List<String> postTimes) {
        List<String> times = new ArrayList<>(postTimes);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String now = LocalDateTime.now().format(formatter);
        int total = 0;

        // 오류 예방
        if (times.size() % 2 != 0) { // END가 아닌 상태 -> 현재 시간 추가
            times.add(now);
        }

        // 총 공부 시간 계산
        for (int i = 1; i < times.size(); i += 2) {
            String[] end = times.get(i).split(":");
            int endHour = Integer.parseInt(end[0]);
            int endMin = Integer.parseInt(end[1]);
            String[] start = times.get(i - 1).split(":");
            int startHour = Integer.parseInt(start[0]);
            int startMin = Integer.parseInt(start[1]);

            if (0 <= endHour & endHour < 5) { // 0~5시 사이의 시간 처리
                endHour += 24;
            }
            if (0 <= startHour & startHour < 5) {
                startHour += 24;
            }
            int temp = (endHour * 60 + endMin) - (startHour * 60 + startMin);
            if (temp > 0) total += temp;
        }

        return new Times(total);
    }
}
