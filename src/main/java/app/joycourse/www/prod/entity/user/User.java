package app.joycourse.www.prod.entity.user;

import app.joycourse.www.prod.entity.ImageFile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(unique = true, nullable = false)
    private String uid;

    @Column(unique = true, length = 10)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private AgeRange ageRange;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private Boolean isSigned = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.NORMAL;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private ImageFile imageFile;

    @CreatedDate
    private LocalDateTime createdAt;

    @PrePersist
    void preInsert() {
        if (Objects.isNull(this.createdAt)) {
            this.createdAt = LocalDateTime.now();
        }
    }
}

