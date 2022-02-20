package app.joycourse.www.prod.entity.auth;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq; // 자동으로 생성되는 seq

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private String uid;

    @Column(nullable = false)
    private String deviceId;
}
