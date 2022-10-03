package talkwith.semogong.repository.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import talkwith.semogong.domain.entity.Post;
import talkwith.semogong.domain.etc.SearchCond;

public interface PostRepositoryCustom {

    Page<Post> findBySearch(SearchCond cond, Pageable pageable);
}
