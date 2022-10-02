package talkwith.semogong.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import talkwith.semogong.domain.entity.Post;


public interface PostRepository extends JpaRepository<Post, Long> {

}
