package app.joycourse.www.prod.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ImageFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq; // 자동으로 생성되는 id, 향후 외래키 고려하여 작성

    @Column(nullable = false)
    private String originalName; // 원본 파일 이름

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ImageFileType imageFileType; // 이미지가 어떤 도메인에서 사용되는지 PROFILE, COURSE

    @Column(unique = true, nullable = false)
    private String hashedName; // 파일의 고유한 해싱된 값. 해싱된 이름 + 확장자

    @Column(nullable = false)
    private String fileType; // 확장자

    @Column(nullable = false)
    private String fileUrl; // 파일 다운로드 url

    @Column(nullable = false)
    private Long sizeByte; // 파일 크기
}
