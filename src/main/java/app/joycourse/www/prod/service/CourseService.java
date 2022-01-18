package app.joycourse.www.prod.service;

import app.joycourse.www.prod.domain.Course;
import app.joycourse.www.prod.domain.CourseDetail;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.repository.CourseDetailRepository;
import app.joycourse.www.prod.repository.CourseRepository;
import app.joycourse.www.prod.repository.JpaCourseDetailRepository;
import app.joycourse.www.prod.repository.JpaCourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Course newCourse = courseRepository.saveCourse(course);

        System.out.println("######courseList: "+course.getCourseDetail().get(0).getId());
        return newCourse;
    }
}
