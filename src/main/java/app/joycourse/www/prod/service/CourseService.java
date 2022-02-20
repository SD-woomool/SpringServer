package app.joycourse.www.prod.service;

import app.joycourse.www.prod.dto.CourseInfoDto;
import app.joycourse.www.prod.dto.CourseListDto;
import app.joycourse.www.prod.dto.PlaceInfoDto;
import app.joycourse.www.prod.entity.Course;
import app.joycourse.www.prod.entity.CourseDetail;
import app.joycourse.www.prod.entity.Place;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.repository.CourseDetailRepository;
import app.joycourse.www.prod.repository.CourseRepository;
import app.joycourse.www.prod.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseDetailRepository courseDetailRepository;
    private final PlaceRepository placeRepository;


    public Course saveCourse(User user, CourseInfoDto courseInfo) {
        Course course = new Course(courseInfo);
        course.setUser(user);
        courseInfo.getCourseDetail().stream().filter(Objects::nonNull).forEach((detailDto) -> {
            PlaceInfoDto placeDto = detailDto.getPlace();
            if (placeDto == null) {
                throw new CustomException(CustomException.CustomError.MISSING_PARAMETERS);
            }
            Place place = placeRepository.findById(placeDto.getId()).orElseThrow(() -> new CustomException(CustomException.CustomError.INVALID_PARAMETER));
            CourseDetail courseDetail = detailDto.convertToEntity();
            courseDetail.setCourse(course);
            courseDetail.setPlace(place);
            course.addCourseDetail(courseDetail);
            if (place != null) {
                place.setCourseDetails(courseDetail);
            }
        });
        course.setTotalPrice();
        return courseRepository.saveCourse(course);
    }

    public CourseListDto pagingCourse(int pageLength, int page) {
        List<CourseInfoDto> courseInfoList = new ArrayList<>();
        courseRepository.pagingById(pageLength, page).stream().flatMap(Collection::stream)
                .forEach((course) ->
                        courseInfoList.add(new CourseInfoDto(course))
                );
        return new CourseListDto(
                courseInfoList.size() < pageLength,
                pageLength,
                page,
                courseInfoList
        );
    }

    public CourseListDto pagingMyCourse(User user, int pageLength, int page) { // 여기서 dto를 작성해서 isend 이런거 다하는거 어떰?
        List<CourseInfoDto> courseInfoList = new ArrayList<>();
        courseRepository.pagingByUser(user, pageLength, page).stream().flatMap(Collection::stream)
                .forEach((course) ->
                        courseInfoList.add(new CourseInfoDto(course))
                );
        return new CourseListDto(
                courseInfoList.size() < pageLength,
                pageLength,
                page,
                courseInfoList
        );
    }

    public void deleteCourse(User user, long courseId) {
        Course deleteCourse = courseRepository.findById(courseId).orElse(null);
        if (deleteCourse == null || deleteCourse.getUser() != user) {
            throw new CustomException(CustomException.CustomError.INVALID_PARAMETER);
        }
        courseRepository.deleteCourse(deleteCourse);
    }

    public Course getCourse(Long courseId) throws CustomException {
        return courseRepository.findById(courseId).orElseThrow(() ->
                new CustomException(CustomException.CustomError.INVALID_PARAMETER));
    }

    public void updateCourse(Course course, CourseInfoDto newCourseInfo) {
        Course newCourse = new Course(newCourseInfo);
        newCourseInfo.getCourseDetail().stream().filter(Objects::nonNull).forEach((detailDto) -> {
            PlaceInfoDto placeDto = detailDto.getPlace();
            if (placeDto == null) {
                throw new CustomException(CustomException.CustomError.MISSING_PARAMETERS);
            }
            Place place = placeRepository.findById(placeDto.getId()).orElseThrow(() -> new CustomException(CustomException.CustomError.INVALID_PARAMETER));
            CourseDetail courseDetail = detailDto.convertToEntity();
            courseDetail.setPlace(place);
            courseDetail.setCourse(newCourse);
            newCourse.addCourseDetail(courseDetail);
            if (place != null) {
                place.setCourseDetails(courseDetail);
            }
        });
        if (!(course.getId().equals(newCourseInfo.getId()))) {
            throw new CustomException(CustomException.CustomError.INVALID_PARAMETER);
        }
        newCourse.setUser(course.getUser());
        newCourse.setLikeCnt(course.getLikeCnt());
        courseRepository.mergeCourse(newCourse);
    }

}
