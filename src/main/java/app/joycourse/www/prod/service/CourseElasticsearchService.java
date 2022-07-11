package app.joycourse.www.prod.service;

import app.joycourse.www.prod.dto.CourseInfoDto;
import app.joycourse.www.prod.dto.CourseListDto;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        return es.index(indexQuery, indexCoordinates);
    }

    public CourseListDto searchCourseByQuery(String query) {
        IndexCoordinates indexCoordinates = es.getIndexCoordinatesFor(CourseInfoDto.class);
        QueryBuilder queryBuilder = QueryBuilders
                .multiMatchQuery(query, "userNickname", "content", "title", "courseDetailDtoList.place.addressName")
                .fuzziness(Fuzziness.AUTO);
        Query searchQuery = new NativeSearchQueryBuilder().withFilter(queryBuilder).build();

        SearchHits<CourseInfoDto> courseHits = es.search(searchQuery, CourseInfoDto.class, indexCoordinates);
        List<CourseInfoDto> courseMatches = new ArrayList<>();
        courseHits.forEach(courseHit -> {
            courseMatches.add(courseHit.getContent());
        });
        return new CourseListDto(
                true,
                courseMatches.size(),
                1,
                courseMatches
        );
    }

}
