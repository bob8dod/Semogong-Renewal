package talkwith.semogong.domain.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Follow extends BaseEntity{

    @Id @GeneratedValue
    @Column(name = "follow_id")
    private long id;

    @ManyToOne
    @JoinColumn(name = "following")
    private Member following; // 팔로 하고자 하는 사람

    @ManyToOne
    @JoinColumn(name = "follower")
    private Member follower; // 팔로를 당한 사람

    // 팔로우, 팔로잉 관계 생성 메서드
    public static Follow create(Member following, Member follower) {
        Follow follow = new Follow();
        follow.following = following;
        follow.follower = follower;
        return follow;
    }
}
