package app.joycourse.www.prod.entity;

import app.joycourse.www.prod.dto.CourseInfoDto;
import app.joycourse.www.prod.entity.user.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private List<CourseDetail> courseDetailList;

    @OneToMany(cascade = CascadeType.REMOVE,
            mappedBy = "course",
            fetch = FetchType.LAZY
    )
    private List<Comment> comments;

    public Course(CourseInfoDto courseInfo) {
        this.id = courseInfo.getId();
        this.title = courseInfo.getTitle();
        this.content = courseInfo.getContent();
        this.likeCnt = courseInfo.getLikeCnt();
        this.location = courseInfo.getLocation();
        this.thumbnailUrl = courseInfo.getThumbnailUrl();
        this.memo = courseInfo.getMemo();
        this.totalPrice = courseInfo.getTotalPrice();
    }

    public static Course of(CourseInfoDto courseInfoDto, User user, List<CourseDetail> courseDetailList, List<Comment> comments) {
        return Course.builder()
                .id(courseInfoDto.getId())
                .user(user)
                .title(courseInfoDto.getTitle())
                .content(courseInfoDto.getContent())
                .likeCnt(courseInfoDto.getLikeCnt())
                .location(courseInfoDto.getLocation())
                .thumbnailUrl(courseInfoDto.getThumbnailUrl())
                .memo(courseInfoDto.getMemo())
                .totalPrice(courseInfoDto.getTotalPrice())
                .courseDetailList(courseDetailList)
                .comments(comments)
                .build();
    }


    public void setCourseDetail(List<CourseDetail> newCourseDetail) {
        if (this.courseDetailList != null) {
            this.courseDetailList.clear();
        }
        this.courseDetailList = newCourseDetail;
    }

    public void addCourseDetail(CourseDetail courseDetail) {
        if (this.courseDetailList == null) {
            this.courseDetailList = new ArrayList<>();
        }
        this.courseDetailList.add(courseDetail);
    }

    public void setTotalPrice() {
        double totalPrice = this.courseDetailList.stream().filter((detail) -> detail.getPrice() != null).
                mapToDouble(CourseDetail::getPrice).sum();
        this.totalPrice = (float) totalPrice;
    }
}



