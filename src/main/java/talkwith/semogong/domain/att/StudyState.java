package talkwith.semogong.domain.att;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StudyState {
    STUDYING("공부 중"), BREAKING("휴식 중"), END("공부 끝");
    private final String state;
}
