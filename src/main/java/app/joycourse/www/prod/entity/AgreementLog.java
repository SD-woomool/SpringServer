package app.joycourse.www.prod.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
public class AgreementLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long agreementSeq;
    private Long userSeq;

    @CreatedDate
    private LocalDateTime createdAt;

    @PrePersist
    void preInsert() {
        if (Objects.isNull(this.createdAt)) {
            this.createdAt = LocalDateTime.now();
        }
    }
}
