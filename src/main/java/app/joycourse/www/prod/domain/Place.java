package app.joycourse.www.prod.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@AllArgsConstructor
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_id")
    private Long id;
    @Column
    private Float x;
    @Column
    private Float y;
    @Column
    private String placeName;
    @Column
    private String categoryName;
    @Column
    private String categoryGroupCode;
    @Column
    private String categoryGroupName;
    @Column
    private String phone;
    @Column
    private String addressName;
    @Column
    private String roadAddressName;
    @Column
    private String placeUrl;
    @Column
    private Float distance;
    @ManyToMany
    @JoinColumn(name = "course_detail_id")
    private List<CourseDetail> courseDetails;


    public void setCourseDetails(CourseDetail courseDetail) {
        if (this.courseDetails == null) {
            this.courseDetails = new ArrayList<CourseDetail>();
        }
        this.courseDetails.add(courseDetail);
    }
}
