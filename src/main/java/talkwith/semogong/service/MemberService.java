package talkwith.semogong.service;

import lombok.RequiredArgsConstructor;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.repository.member.MemberRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void save(Member member) {
        /* Spring Security를 통한 암호화 */
        // BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        // member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.save(member);
    }


}
