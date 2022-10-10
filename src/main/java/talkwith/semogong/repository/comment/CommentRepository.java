package talkwith.semogong.repository.comment;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import talkwith.semogong.domain.entity.Comment;
import talkwith.semogong.domain.entity.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // post에 해당 하는 comment list 찾기
    @EntityGraph(attributePaths = {"member"})
    List<Comment> findAllByPostOrderByCreatedDateDesc(Post post);
}
