package app.joycourse.www.prod.repository;


import app.joycourse.www.prod.entity.elasticsearch.CourseDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CourseDocumentRepository extends ElasticsearchRepository<CourseDocument, String> {

}
