package app.joycourse.www.prod.service;

import app.joycourse.www.prod.domain.Course;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.dto.CourseInfoDto;
import app.joycourse.www.prod.dto.CourseListDto;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.repository.CourseDetailRepository;
import app.joycourse.www.prod.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseDetailRepository courseDetailRepository;

    public CourseService(CourseRepository jpaCourseRepository, CourseDetailRepository jpaCourseDetailRepository) {
        this.courseRepository = jpaCourseRepository;
        this.courseDetailRepository = jpaCourseDetailRepository;
    }

    public Course saveCourse(User user, Course course) {
        course.setUser(user);
        course.setTotalPrice();
        course.getCourseDetail().forEach((detail) -> {
            detail.setCourse(course);
            courseDetailRepository.saveCourseDetail(detail);
        });
        return courseRepository.saveCourse(course);
    }

    public CourseListDto pagingCourse(int pageLength, int page) {
        List<CourseInfoDto> courseInfoList = new ArrayList<>();
        courseRepository.pagingById(pageLength, page).stream().flatMap(Collection::stream)
                .forEach((course) -> {
                    courseInfoList.add(new CourseInfoDto(course));
                });
        return new CourseListDto(
                courseInfoList.size() < pageLength,
                pageLength,
                page,
                courseInfoList
        );
    }

    public CourseListDto pagingMyCourse(User user, int pageLength, int page) {
        List<CourseInfoDto> courseInfoList = new ArrayList<>();
        courseRepository.pagingByUser(user, pageLength, page).stream().flatMap(Collection::stream)
                .forEach((course) -> {
                    courseInfoList.add(new CourseInfoDto(course));
                });
        return new CourseListDto(
                courseInfoList.size() < pageLength,
                pageLength,
                page,
                courseInfoList
        );
    }

    public void deleteCourse(User user, long courseId) {
        Course deleteCourse = courseRepository.findById(courseId).orElse(null);
        if (deleteCourse == null && deleteCourse.getUser() == user) {
            throw new CustomException("WRONG_COURSE_ID", CustomException.CustomError.INVALID_PARAMETER);
        }
        courseRepository.deleteCourse(deleteCourse);
    }

    public Course getCourse(Long courseId) throws CustomException {
        return courseRepository.findById(courseId).orElseThrow(() ->
                new CustomException("INVALID COURSE_ID", CustomException.CustomError.INVALID_PARAMETER));
    }

    public void updateCourse(Course course, Course newCourseInfo) {
        newCourseInfo.getCourseDetail().stream().filter(Objects::nonNull).forEach((detail) -> {
            detail.setCourse(course);
        });
        if (!(course.getId().equals(newCourseInfo.getId()))) {
            throw new CustomException("INVALID_COURSE_INFO", CustomException.CustomError.INVALID_PARAMETER);
        }
        newCourseInfo.setUser(course.getUser());
        newCourseInfo.setLikeCnt(course.getLikeCnt());
        courseRepository.mergeCourse(newCourseInfo);
    }

}
