package app.joycourse.www.prod.service;

import app.joycourse.www.prod.dto.CourseInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseElasticsearchService {
    private final ElasticsearchOperations es;

    public String save(CourseInfoDto course) {
        IndexCoordinates indexCoordinates = es.getIndexCoordinatesFor(CourseInfoDto.class);
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(course.getId().toString())
                .withObject(course)
                .build();
        return es.index(indexQuery, indexCoordinates); // 이거에 대해서 좀 공부해야할듯
    }
}
