package talkwith.semogong.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import talkwith.semogong.domain.entity.Follow;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class FollowRepository {

    private final EntityManager em;

    public void save(Follow follow) {
        em.persist(follow);
    }
}
