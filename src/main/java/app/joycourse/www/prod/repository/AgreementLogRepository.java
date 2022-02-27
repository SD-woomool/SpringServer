package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.entity.AgreementLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgreementLogRepository extends JpaRepository<AgreementLog, Long> {
    Optional<AgreementLog> findByAgreementSeqAndUserSeq(Long agreementSeq, Long userSeq);
}
