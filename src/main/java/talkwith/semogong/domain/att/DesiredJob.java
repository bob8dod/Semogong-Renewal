package talkwith.semogong.domain.att;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DesiredJob {
    // 추가 가능
    Backend("백엔드 엔지니어"), Frontend("프론트엔드 엔지니어"), DataEngineer("데이터 엔지니어"),
    MlEngineer("ML 엔지니어"), DlEngineer("DL 엔지니어");
    private final String describe;

}
