package talkwith.semogong.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import talkwith.semogong.domain.entity.Follow;
import talkwith.semogong.domain.entity.Member;

import java.util.List;


public interface FollowRepository extends JpaRepository<Follow, Long> {

}
