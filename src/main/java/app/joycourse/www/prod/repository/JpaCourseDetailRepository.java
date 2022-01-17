package app.joycourse.www.prod.repository;

import app.joycourse.www.prod.domain.CourseDetail;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class JpaCourseDetailRepository implements CourseDetailRepository{

    private EntityManager em;

    public JpaCourseDetailRepository(EntityManager em){
        this.em = em;
    }

    @Override
    public CourseDetail saveCourseDetail(CourseDetail courseDetail){
        em.persist(courseDetail);
        return courseDetail;
    }
}
