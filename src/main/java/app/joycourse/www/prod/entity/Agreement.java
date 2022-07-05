package app.joycourse.www.prod.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq; // 자동으로 생성되는 seq
    private String title; // 약관 제목
    private String content; // 약관 내용
    private Boolean isRequired; // true: 필수, false: 선택
}
