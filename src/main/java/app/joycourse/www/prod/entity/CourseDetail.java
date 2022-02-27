package app.joycourse.www.prod.entity;

import app.joycourse.www.prod.dto.CourseDetailDto;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;


@Entity
@Getter
@Setter
public class CourseDetail {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private Place place;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_detail_id")
    private Long id;

    @Column
    @ColumnDefault("0")
    private Float price;

    private String photo;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

    public CourseDetail() {

    }

    public CourseDetail(CourseDetailDto courseDetailDto) {
        this.price = courseDetailDto.getPrice();
        this.photo = courseDetailDto.getPhoto() == null ? null : courseDetailDto.getPhoto().getFileName();
        this.content = courseDetailDto.getContent();
    }

}
