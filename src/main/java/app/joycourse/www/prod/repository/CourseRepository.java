package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.domain.Course;
import app.joycourse.www.prod.domain.User;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    Course saveCourse(Course course);
    Optional<List<Course>> findByUser(User user, int pageLength, int page);
}
