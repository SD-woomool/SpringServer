package app.joycourse.www.prod.service;

import app.joycourse.www.prod.domain.Course;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.repository.CourseRepository;
import app.joycourse.www.prod.repository.JpaCourseRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    CourseRepository courseRepository;

    public CourseService(JpaCourseRepository jpaCourseRepository){
        this.courseRepository = jpaCourseRepository;
    }

    public Course saveCourse(User user, Course course){
        course.setUser(user);
        Course newCourse = courseRepository.saveCourse(course);
        return newCourse;
    }
}
