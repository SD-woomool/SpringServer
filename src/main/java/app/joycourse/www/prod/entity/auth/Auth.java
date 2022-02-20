package app.joycourse.www.prod.entity.auth;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Auth {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq; // 자동으로 생성되는 seq

    @Column(unique = true, nullable = false)
    private String uid; // 유저를 구분하는 uid, PROVIDER_uid 맨앞은 provider로 구분짓는다. (ex: NAVER_128989875753 )

    @Column(unique = true, nullable = false)
    private String email; // 유저 email, 기존에 oauth 로그인 했는지 체크한다.

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider; // oauth provider

    private LocalDateTime createdAt; // 생성 일자

    private LocalDateTime lastLoginAt; // 마지막 로그인 시점

    @PrePersist
    void preInsert() {
        LocalDateTime now = LocalDateTime.now();
        if (Objects.isNull(this.createdAt)) {
            this.createdAt = now;
        }
        if (Objects.isNull(this.lastLoginAt)) {
            this.lastLoginAt = now;
        }
    }

    @PreUpdate
    void preUpdate() {
        this.lastLoginAt = LocalDateTime.now();
    }
}
