package talkwith.semogong.domain.att;

import lombok.AllArgsConstructor;
import lombok.Getter;
import talkwith.semogong.domain.etc.Times;

import javax.persistence.Embeddable;

@Getter
@AllArgsConstructor
public enum Tier {
    DIAMOND("다이아"), PLATINUM("플레티넘"), GOLD("골드"), SILVER("실버"), IRON("아이언"), STONES("스톤즈");
    private final String describe;

    public static Tier evaluate(long totalTime) {
        return DIAMOND;
    }
}
