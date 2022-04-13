package app.joycourse.www.prod.entity;

import app.joycourse.www.prod.dto.CourseDetailDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;


@Entity
@Getter
@Setter
@Builder
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

    public CourseDetail() {

    }

    public CourseDetail(CourseDetailDto courseDetailDto) {
        this.price = courseDetailDto.getPrice();
        this.photo = courseDetailDto.getPhoto() == null ? null : courseDetailDto.getPhoto().getFileName();
        this.content = courseDetailDto.getContent();
    }

    /*
     * Place를 어떡하지? 따로 만들어서 넣어주는게 맞나?
     * 엔티티에 서비스를 주입해서 찾는건 아닌거 같은데
     */
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
