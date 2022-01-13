package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.domain.Course;
import app.joycourse.www.prod.domain.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

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
    public List<Course> findByUser(User user){
        Course course = new Course();
        List<Course> list = new ArrayList<>();
        list.add(course);
        return list;
    }
}
