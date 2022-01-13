package app.joycourse.www.prod.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)  // 이거 타입 알보자
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private Integer likeCnt;

    @Column
    private Float totalPrice;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "course_detail_id")
    private CourseDetail courseDetail;
}
