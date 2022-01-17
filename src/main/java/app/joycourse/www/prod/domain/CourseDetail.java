package app.joycourse.www.prod.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Getter
@Setter
public class CourseDetail {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "course_detail_id")
    private Integer id;

    @Column
    private Float price;

    @Column(nullable = true)
    private String photo;

    @Column(nullable = true)
    private String content;

    @Column(nullable = true)
    private String location;

    @Column(nullable = true)
    private String thumbnailUrl;

    @Column(nullable = true)
    private String memo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;


}
