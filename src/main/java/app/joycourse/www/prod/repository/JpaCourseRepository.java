package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.entity.Course;
import app.joycourse.www.prod.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaCourseRepository implements CourseRepository {

    private final EntityManager em;

    public JpaCourseRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public void deleteCourse(Course deleteCourse) {
        em.remove(deleteCourse);
    }

    @Override
    public Course saveCourse(Course course) {
        em.persist(course);
        return course;
    }

    @Override
    public Optional<List<Course>> pagingByUser(User user, int pageLength, int page) { // 이름 바꿔야할듯?
        List<Course> result = em.createQuery("select c from Course c where c.user = :user", Course.class).
                setParameter("user", user).
                setMaxResults(pageLength).
                setFirstResult((page - 1) * pageLength).
                getResultList();
        return Optional.ofNullable(result);
    }

    @Override
    public Optional<List<Course>> pagingById(int pageLength, int page) {
        List<Course> result = em.createQuery("select c from Course c", Course.class)
                .setMaxResults(pageLength)
                .setFirstResult((page - 1) * pageLength)
                .getResultList();
        return Optional.ofNullable(result);
    }


    @Override
    public Optional<Course> findById(Long courseId) {
        return Optional.ofNullable(em.find(Course.class, courseId));
    }

    @Override
    public void mergeCourse(Course courseInfo) {
        em.merge(courseInfo);
    }


}
