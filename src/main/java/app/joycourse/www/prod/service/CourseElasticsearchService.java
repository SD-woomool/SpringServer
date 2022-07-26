package app.joycourse.www.prod.service;

import app.joycourse.www.prod.dto.CourseInfoDto;
import app.joycourse.www.prod.dto.CourseListDto;
import app.joycourse.www.prod.exception.CustomException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class CourseElasticsearchService {
    private final ElasticsearchOperations es;

    private boolean createIndex(Class<?> clazz) throws IOException {
        IndexOperations indexOperations = es.indexOps(clazz);

        return indexOperations.createWithMapping();
    }

    public String save(CourseInfoDto course) throws IOException {
        IndexCoordinates indexCoordinates = es.getIndexCoordinatesFor(CourseInfoDto.class);
        IndexOperations indexOperations = es.indexOps(indexCoordinates);
        if (!indexOperations.exists()) {
            if (!createIndex(CourseInfoDto.class)) {
                throw new CustomException(CustomException.CustomError.SERVER_ERROR);
            }
        }
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(course.getId().toString())
                .withObject(course)
                .build();
        return es.index(indexQuery, indexCoordinates);
    }

    public String delete(CourseInfoDto course) {
        IndexCoordinates indexCoordinates = es.getIndexCoordinatesFor(CourseInfoDto.class);
        return es.delete(course, indexCoordinates);
    }

    public CourseListDto searchCourseByQuery(String query, int page, int size) {
        IndexCoordinates indexCoordinates = es.getIndexCoordinatesFor(CourseInfoDto.class);
        QueryBuilder queryBuilder = buildSearchQuery(query);
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(page, size))
                .build();
        List<CourseInfoDto> courseHits = es.search(searchQuery, CourseInfoDto.class, indexCoordinates)
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
        return new CourseListDto(
                courseHits.size() < size,
                courseHits.size(),
                page,
                courseHits
        );
    }

    public CourseListDto searchCourseByGeoQuery(String query, double[] leftTop, double[] rightBottom, int page, int size) {
        IndexCoordinates indexCoordinates = es.getIndexCoordinatesFor(CourseInfoDto.class);
        QueryBuilder queryBuilder = buildGoeSearchQuery(query, leftTop, rightBottom);
        Query searchQuery = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(page, size))
                .build();
        List<CourseInfoDto> courseHits = es.search(searchQuery, CourseInfoDto.class, indexCoordinates)
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
        return new CourseListDto(
                courseHits.size() < size,
                courseHits.size(),
                page,
                courseHits
        );
    }


    private QueryBuilder buildGoeSearchQuery(String query, double[] leftTop, double[] rightBottom) {
        return QueryBuilders.boolQuery()
                .filter(QueryBuilders.geoBoundingBoxQuery("courseDetailDtoList.place.location").setCorners(
                        leftTop[1],
                        leftTop[0],
                        rightBottom[1],
                        rightBottom[0]
                ))
                .must(QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchQuery("title", query)).boost(40.0f)
                        .should(QueryBuilders.matchQuery("title.nori", query)).boost(30.0f)
                        .should(QueryBuilders.matchQuery("content", query)).boost(20.0f)
                        .should(QueryBuilders.matchQuery("userNickname", query)).boost(3.0f)
                        .should(QueryBuilders.matchQuery("userNickname.keyword", query)).boost(10.0f)
                        .should(QueryBuilders.matchQuery("courseDetailDtoList.place.addressName", query)).boost(35.0f)
                        .should(QueryBuilders.matchQuery("courseDetailDtoList.place.addressName.nori", query)).boost(15.0f)
                        .should(QueryBuilders.matchQuery("courseDetailDtoList.place.placeName", query)).boost(25.0f)
                        .should(QueryBuilders.matchQuery("courseDetailDtoList.place.placeName.nori", query)).boost(15.0f)
                        .should(QueryBuilders.matchQuery("courseDetailDtoList.content", query)).boost(6.0f)
                );
    }

    private QueryBuilder buildSearchQuery(String query) {
        return QueryBuilders.boolQuery()
                .must(QueryBuilders.boolQuery()
                        .should(QueryBuilders.matchQuery("title", query)).boost(40.0f)
                        .should(QueryBuilders.matchQuery("title.nori", query)).boost(30.0f)
                        .should(QueryBuilders.matchQuery("content", query)).boost(20.0f)
                        .should(QueryBuilders.matchQuery("userNickname", query)).boost(3.0f)
                        .should(QueryBuilders.matchQuery("userNickname.keyword", query)).boost(10.0f)
                        .should(QueryBuilders.matchQuery("courseDetailDtoList.place.addressName", query)).boost(35.0f)
                        .should(QueryBuilders.matchQuery("courseDetailDtoList.place.addressName.nori", query)).boost(15.0f)
                        .should(QueryBuilders.matchQuery("courseDetailDtoList.place.placeName", query)).boost(25.0f)
                        .should(QueryBuilders.matchQuery("courseDetailDtoList.place.placeName.nori", query)).boost(15.0f)
                        .should(QueryBuilders.matchQuery("courseDetailDtoList.content", query)).boost(6.0f)
                );
    }

}
