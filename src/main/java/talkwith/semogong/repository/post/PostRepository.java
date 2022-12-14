package talkwith.semogong.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import talkwith.semogong.domain.att.StudyState;
import talkwith.semogong.domain.dto.post.PostViewDto;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.domain.entity.Post;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Override
    @EntityGraph(attributePaths = {"member"})
    Page<Post> findAll(Pageable pageable); // 전체 글 조회

    /*    // Member의 최신 글 조회
    @EntityGraph(attributePaths = {"member"})
    Optional<Post> findFirstByMember(Member member, Sort sort);*/

    // Member의 최신 글 조회
    @EntityGraph(attributePaths = {"member"})
    Optional<Post> findFirstByMemberAndCustomDate(Member member, LocalDate date);

    // Member의 전체 글 조회
    @EntityGraph(attributePaths = {"member"})
    Page<Post> findAllByMember(Member member, Pageable pageable);

    // 지정 날짜 사이의 글 조회
    @EntityGraph(attributePaths = {"member"})
    List<Post> findAllByMemberAndCustomDateBetween(Member member, LocalDate from, LocalDate to);

    @EntityGraph(attributePaths = {"member"})
    List<Post> findAllByCustomDateOrderByTotalTimeDesc(LocalDate date);
}
