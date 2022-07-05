package app.joycourse.www.prod.service;

import app.joycourse.www.prod.dto.request.AgreementRequestDto;
import app.joycourse.www.prod.dto.request.AgreementRequestDto.AgreementRequest;
import app.joycourse.www.prod.dto.response.AgreementResponseDto;
import app.joycourse.www.prod.entity.Agreement;
import app.joycourse.www.prod.entity.AgreementLog;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.repository.AgreementLogRepository;
import app.joycourse.www.prod.repository.AgreementRepository;
import app.joycourse.www.prod.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AgreementService {
    private final UserRepository userRepository;
    private final AgreementRepository agreementRepository;
    private final AgreementLogRepository agreementLogRepository;

    // 약관중에서 필수인데 아직 동의하지 않은 경우 내려준다.
    // 해당 경우 선택 약관도 같이 내려준다.
    public List<AgreementResponseDto> getAgreements(User user) {
        List<Agreement> agreements = agreementRepository.findAll();
        boolean isNeedAgreement = agreements.stream()
                .filter(Agreement::getIsRequired)
                .map(agreement -> agreementLogRepository.findByAgreementSeqAndUserSeq(agreement.getSeq(), user.getSeq()))
                .anyMatch(Optional::isEmpty);

        if (isNeedAgreement) {
            return agreements.stream()
                    .map(agreement -> AgreementResponseDto.builder()
                            .seq(agreement.getSeq())
                            .title(agreement.getTitle())
                            .content(agreement.getContent())
                            .isRequired(agreement.getIsRequired())
                            .build())
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional
    public void agreeAgreements(User user, AgreementRequestDto agreementRequestDto) {
        List<AgreementRequest> agreementsRequest = agreementRequestDto.getAgreements();
        List<Long> seqList = agreementsRequest.stream().map(AgreementRequest::getAgreementSeq).collect(Collectors.toList());
        List<Agreement> agreements = agreementRepository.findAll();

        if (seqList.size() != agreements.size()) {
            // 잘못된 약관 번호인 경우
            log.error("[agreeAgreements] wrong agreement Seq.");
            throw new CustomException(CustomException.CustomError.INVALID_PARAMETER);
        }

        agreements.forEach(agreement -> {
            // 필터로 해당 약관은 무조건 있을것이다. 바로 위에 이프문에서 검증하기때문이다.
            Optional<AgreementRequest> optionalAgreementRequest = agreementsRequest.stream().filter(ar -> ar.getAgreementSeq().equals(agreement.getSeq())).findAny();
            if (agreement.getIsRequired() && (optionalAgreementRequest.isEmpty() || !optionalAgreementRequest.get().getIsAgree())) {
                // 필수 약관을 동의하지 않은 경우, 프론트에서 1차적으로 막아야한다.
                log.error("[agreeAgreements] Should agree required agreement.");
                throw new CustomException(CustomException.CustomError.SHOULD_AGREE);
            }

            // 하나라도 오류나면 Transactional 때문에 rollback 될것이다.
            AgreementLog agreementLog = new AgreementLog();
            agreementLog.setAgreementSeq(agreement.getSeq());
            agreementLog.setUserSeq(user.getSeq());
            agreementLogRepository.save(agreementLog);
        });

        user.setIsAgreed(true);
        userRepository.save(user);
    }

}
