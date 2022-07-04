package app.joycourse.www.prod.entity;


import app.joycourse.www.prod.dto.PlaceInfoDto;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
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

    @OneToMany(cascade = CascadeType.REMOVE,//{CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},
            mappedBy = "place",
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    @Column(name = "course_detail_id")
    private List<CourseDetail> courseDetails;


    public static Place of(PlaceInfoDto placeInfo, List<CourseDetail> courseDetailList) {
        return Place.builder()
                .id(placeInfo.getId())
                .x(placeInfo.getX())
                .y(placeInfo.getY())
                .placeName(placeInfo.getPlaceName())
                .categoryName(placeInfo.getCategoryName())
                .categoryGroupCode(placeInfo.getCategoryGroupCode())
                .categoryGroupName(placeInfo.getCategoryGroupName())
                .phone(placeInfo.getPhone())
                .addressName(placeInfo.getAddressName())
                .roadAddressName(placeInfo.getRoadAddressName())
                .distance(placeInfo.getDistance())
                .courseDetails(courseDetailList)
                .build();
    }

    public void setCourseDetails(CourseDetail courseDetail) {
        if (this.courseDetails == null) {
            this.courseDetails = new ArrayList<>();
        }
        this.courseDetails.add(courseDetail);
    }
}
