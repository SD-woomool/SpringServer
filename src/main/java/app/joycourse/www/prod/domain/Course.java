package app.joycourse.www.prod.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;

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
    @ColumnDefault("0")
    private Integer likeCnt;

    @Column(nullable = true)
    private String location;

    @Column(nullable = true)
    private String thumbnailUrl;

    @Column(nullable = true)
    private String memo;

    @Column
    @ColumnDefault("0")
    private Float totalPrice;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "course")
    @Column(nullable = false)
    private List<CourseDetail> courseDetail;

    public void setTotalPrice(){   // 좀 허접해 다시 해
        double totalPrice = 0;
        totalPrice = this.courseDetail.stream().filter((detail) -> detail.getPrice() != null).
                mapToDouble(CourseDetail::getPrice).sum();
        this.totalPrice = (float) totalPrice;
    }
}



