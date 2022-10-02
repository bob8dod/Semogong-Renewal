package talkwith.semogong.custom.annotation;

import lombok.RequiredArgsConstructor;
import talkwith.semogong.repository.member.MemberRepository;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.MessageFormat;

@RequiredArgsConstructor
public class LoginIdValidator implements ConstraintValidator<LoginId, String> {

    private final MemberRepository memberRepository;
    private static final int MIN_SIZE = 8;
    private static final int MAX_SIZE = 30;
    private static final String regexLoginId = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{" + MIN_SIZE + "," + MAX_SIZE + "}$";

    @Override
    public boolean isValid(String loginId, ConstraintValidatorContext context) {
        boolean isValidLoginId = loginId.matches(regexLoginId);
        if (!isValidLoginId) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            MessageFormat.format("{0}자 이상의 {1}자 이하의 숫자, 영문자를 포함한 아이디를 입력해주세요", MIN_SIZE, MAX_SIZE))
                    .addConstraintViolation();
        } else if (memberRepository.findOneByLoginId(loginId).isPresent()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            MessageFormat.format("{0}은 이미 존재하는 아이디입니다.", loginId))
                    .addConstraintViolation();
        }
        return isValidLoginId;
    }

    public boolean isValid(String loginId) {
        return false;
    }
}
