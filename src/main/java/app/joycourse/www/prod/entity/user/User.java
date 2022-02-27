package app.joycourse.www.prod.entity.user;

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

    @Column(unique = true, length = 10)
    private String nickname;

    @ColumnDefault("'PRIVATE'")
    @Enumerated(EnumType.STRING)
    private AgeRangeEnum ageRangeEnum = AgeRangeEnum.PRIVATE;

    @ColumnDefault("'PRIVATE'")
    @Enumerated(EnumType.STRING)
    private GenderEnum genderEnum = GenderEnum.PRIVATE;

    private String profileImageUrl;

    @ColumnDefault("false")
    private Boolean isSigned = false;

    @ColumnDefault("'NORMAL'")
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role = UserRoleEnum.NORMAL;

    @CreatedDate
    private LocalDateTime createdAt;

    @PrePersist
    void preInsert() {
        if (Objects.isNull(this.createdAt)) {
            this.createdAt = LocalDateTime.now();
        }
    }
}

