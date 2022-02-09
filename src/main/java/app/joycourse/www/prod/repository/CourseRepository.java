package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.entity.Course;
import app.joycourse.www.prod.entity.User;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    Course saveCourse(Course course);
    Optional<List<Course>> findByUser(User user, int pageLength, int page);
}
