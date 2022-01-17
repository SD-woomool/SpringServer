package app.joycourse.www.prod.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @ManyToOne// 이거 타입 알보자
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private Integer likeCnt;

    @Column
    private Float totalPrice;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_detail_id")
    @Column(name = "course_detail", nullable = false)
    private List<CourseDetail> courseDetail;
}


