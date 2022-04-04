package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.entity.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    List<Agreement> findAllBySeqIn(List<Long> seqList);
}
