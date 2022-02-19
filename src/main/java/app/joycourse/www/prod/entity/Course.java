package app.joycourse.www.prod.entity;

import app.joycourse.www.prod.dto.CourseInfoDto;
import app.joycourse.www.prod.entity.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
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
    @JoinColumn(name = "user_uid")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @ColumnDefault("0")
    private Integer likeCnt;

    private String location;

    private String thumbnailUrl;

    private String memo;

    @Column
    private Float totalPrice;

    @OneToMany(cascade = CascadeType.ALL,//{CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},
            mappedBy = "course",
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @Column(nullable = false)
    private List<CourseDetail> courseDetail;

    @OneToMany(cascade = CascadeType.REMOVE,
            mappedBy = "course",
            fetch = FetchType.LAZY
    )
    private List<Comment> comments;

    public Course() {
    }

    public Course(CourseInfoDto courseInfo) {
        this.id = courseInfo.getId();
        this.title = courseInfo.getTitle();
        this.content = courseInfo.getContent();
        this.likeCnt = courseInfo.getLikeCnt();
        this.location = courseInfo.getLocation();
        this.thumbnailUrl = courseInfo.getThumbnailUrl();
        this.memo = courseInfo.getMemo();
        this.totalPrice = courseInfo.getTotalPrice();
        //this.courseDetail = Optional.ofNullable(courseInfo.getCourseDetail()).stream().flatMap(Collection::stream)
        //        .map(CourseDetailDto::convertToEntity).collect(Collectors.toList());  // 원래 add 하는거 보다 이게 더 나은듯
    }

    public void setCourseDetail(List<CourseDetail> newCourseDetail) {
        if (this.courseDetail != null) {
            this.courseDetail.clear();
        }
        this.courseDetail = newCourseDetail;
    }

    public void addCourseDetail(CourseDetail courseDetail) {
        if (this.courseDetail == null) {
            this.courseDetail = new ArrayList<>();
        }
        this.courseDetail.add(courseDetail);
    }

    public void setTotalPrice() {   // 좀 허접해 다시 해
        double totalPrice = this.courseDetail.stream().filter((detail) -> detail.getPrice() != null).
                mapToDouble(CourseDetail::getPrice).sum();
        this.totalPrice = (float) totalPrice;
    }
}



