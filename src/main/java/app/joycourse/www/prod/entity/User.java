package app.joycourse.www.prod.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
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

    @Column(unique = true)
    private String nickname;

    @ColumnDefault("'PRIVATE'")
    @Enumerated(EnumType.STRING)
    private AgeRange ageRange = AgeRange.PRIVATE;

    @ColumnDefault("'PRIVATE'")
    @Enumerated(EnumType.STRING)
    private Gender gender = Gender.PRIVATE;

    private String profileImageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Provider provider;

    @ColumnDefault("false")
    private Boolean isSigned = false;

    @ColumnDefault("'NONE'")
    @Enumerated(EnumType.STRING)
    private Agreement agreement = Agreement.NONE;

    @ColumnDefault("'NORMAL'")
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.NORMAL;

    @CreatedDate
    private LocalDateTime createdAt;

    @PrePersist
    void preInsert() {
        if (Objects.isNull(this.createdAt)) {
            this.createdAt = LocalDateTime.now();
        }
    }
}

