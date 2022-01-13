package app.joycourse.www.prod.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class CourseDetail {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "course_detail_id")
    private Integer id;

    @Column
    private Float price;
}
