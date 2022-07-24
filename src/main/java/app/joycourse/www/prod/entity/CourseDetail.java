package app.joycourse.www.prod.entity;

import app.joycourse.www.prod.dto.CourseDetailDto;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetail {

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private Place place;

    public static CourseDetail of(CourseDetailDto courseDetailDto, Course course, Place place) {
        String photo = courseDetailDto.getPhoto() != null ? courseDetailDto.getPhoto().getFileUrl() : null;
        return CourseDetail.builder()
                .content(courseDetailDto.getContent())
                .id(courseDetailDto.getId())
                .course(course)
                .place(place)
                .photo(photo)
                .price(courseDetailDto.getPrice())
                .build();
    }

}
