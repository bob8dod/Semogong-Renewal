package talkwith.semogong.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import talkwith.semogong.config.SessionConst;
import talkwith.semogong.domain.dto.member.MemberHomeDto;
import talkwith.semogong.domain.dto.post.PostViewDto;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.domain.entity.Post;
import talkwith.semogong.domain.etc.SearchCond;
import talkwith.semogong.service.MemberService;
import talkwith.semogong.service.PostService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;
    private final MemberService memberService;

    @ModelAttribute("check")
    public boolean check(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Long loginMemberId) {
        return loginMemberId != null;
    }

    @GetMapping("/")
    public String home(@RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "focus", defaultValue = "all") String category,
                       @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Long loginMemberId, Model model){

        // login member 설정
        Optional<Member> loginMember = memberService.findOne(loginMemberId);

        // category 설정
        SearchCond cond = new SearchCond();
        cond.setCategory(category);

        // category에 따른 post
        Page<PostViewDto> posts = postService.findBySearch(cond, loginMember.orElse(null), page - 1);

        // 팔로하는 멤버의 현 상태 확인 (top 5)
        if (loginMember.isPresent()) {
            List<MemberHomeDto> topFollowingMembers = memberService.findTop5Following(loginMember.get());
            MemberHomeDto memberDto = new MemberHomeDto(loginMember.get());
            Optional<PostViewDto> memberRecentPost = postService.findTodayPost(loginMemberId);
        }
        else {List<MemberHomeDto> topFollowingMembers = new ArrayList<>();}

        // 오늘 공부시간 Ranking
        List<MemberHomeDto> memberRanking = memberService.findAll();
        memberRanking.sort(new CustomSorter.Times());
        List<MemberHomeDto> memberRankingResult = memberRanking;
        if (memberRanking.size() >= 5) {
            memberRankingResult = memberRanking.subList(0, 4);
        }
        return "home";
    }

/*    @RequestMapping("/search")
    public String search(@RequestParam(name="category", defaultValue = "all") String category) {
        SearchCond searchCond = new SearchCond();
        searchCond.setCategory(category);
    }*/
}
