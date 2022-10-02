package talkwith.semogong.repository.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import talkwith.semogong.domain.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
