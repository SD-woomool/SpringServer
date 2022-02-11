package app.joycourse.www.prod.domain;

import app.joycourse.www.prod.dto.CourseDetailDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;


@Entity
@Getter
@Setter
public class CourseDetail {

    @ManyToMany(
            fetch = FetchType.LAZY,
            mappedBy = "courseDetails"
    )
    List<Place> places;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_detail_id")
    private Long id;

    @Column
    @ColumnDefault("0")
    private Float price;

    @Column(nullable = true)
    private String photo;

    @Column(nullable = true)
    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

    public CourseDetail() {

    }

    public CourseDetail(CourseDetailDto courseDetailDto) {
        this.price = courseDetailDto.getPrice();
        this.photo = courseDetailDto.getPhoto();
        this.content = courseDetailDto.getContent();
    }

}
