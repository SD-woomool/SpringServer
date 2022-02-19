package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.entity.Course;
import app.joycourse.www.prod.entity.user.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaCourseRepository implements CourseRepository{

    EntityManager em;

    public JpaCourseRepository(EntityManager em){
        this.em = em;
    }

    @Override
    public Course saveCourse(Course course){
        em.persist(course);
        return course;
    }

    @Override
    public Optional<List<Course>> findByUser(User user, int pageLength, int page){ // 이름 바꿔야할듯?
        List<Course> result = em.createQuery("select c from Course c where c.user = :user", Course.class).
                setParameter("user", user).
        setMaxResults(pageLength).
        setFirstResult((page - 1) * pageLength).
                getResultList();
        System.out.println("this is result: " + result);
        return Optional.ofNullable(result);
    }
}
