package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.entity.Course;
import app.joycourse.www.prod.entity.user.User;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    void deleteCourse(Course course);

    Course saveCourse(Course course);

    Optional<List<Course>> pagingByUser(User user, int pageLength, int page);

    Optional<List<Course>> pagingById(int pageLength, int page);

    Optional<Course> findById(Long courseId);

    void mergeCourse(Course courseInfo);
}
