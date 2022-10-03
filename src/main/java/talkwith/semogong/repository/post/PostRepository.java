package talkwith.semogong.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import talkwith.semogong.domain.entity.Member;
import talkwith.semogong.domain.entity.Post;

import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {

    @Override
    @EntityGraph(attributePaths = {"member"})
    Page<Post> findAll(Pageable pageable); // 전체 글 조회

    // Member의 최신 글 조회
    Optional<Post> findFirstByMember(Member member, Sort sort);
    Optional<Post> findFirstByMemberOrderByCreatedDateDesc(Member member);

    // Member의 전체 글 조회
    Page<Post> findAllByMember(Member member, Pageable pageable);

}
