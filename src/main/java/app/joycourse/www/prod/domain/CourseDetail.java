package app.joycourse.www.prod.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;


@Entity
@Getter
@Setter
public class CourseDetail {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "course_detail_id")
    private Long id;

    @Column
    @ColumnDefault("0")
    private Float price;

    @Column(nullable = true)
    private String photo;

    @Column(nullable = true)
    private String content;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

}
