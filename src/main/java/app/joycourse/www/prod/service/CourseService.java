package app.joycourse.www.prod.service;

import app.joycourse.www.prod.domain.Course;
import app.joycourse.www.prod.domain.CourseDetail;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.dto.MyCourseListDto;
import app.joycourse.www.prod.repository.CourseDetailRepository;
import app.joycourse.www.prod.repository.CourseRepository;
import app.joycourse.www.prod.repository.JpaCourseDetailRepository;
import app.joycourse.www.prod.repository.JpaCourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseService {

    CourseRepository courseRepository;
    CourseDetailRepository courseDetailRepository;

    public CourseService(JpaCourseRepository jpaCourseRepository, JpaCourseDetailRepository jpaCourseDetailRepository){
        this.courseRepository = jpaCourseRepository;
        this.courseDetailRepository = jpaCourseDetailRepository;
    }

    public Course saveCourse(User user, Course course){
        course.setUser(user);
        course.setTotalPrice();
        course.getCourseDetail().forEach((detail) -> {
            detail.setCourse(course);
            courseDetailRepository.saveCourseDetail(detail);
        });
        return courseRepository.saveCourse(course);
    }

    public MyCourseListDto pagingMyCourse(User user, int pageLength, int page){ // 여기서 dto를 작성해서 isend 이런거 다하는거 어떰?
        MyCourseListDto myCourseListDto = new MyCourseListDto(false, pageLength, page);
        List<Course> courseList = courseRepository.findByUser(user, pageLength, page).orElse(null);
        if (courseList == null || courseList.size() < pageLength){
            myCourseListDto.setIsEnd(true);
        }
        myCourseListDto.setCourseList(courseList);
        return myCourseListDto;
    }

}
