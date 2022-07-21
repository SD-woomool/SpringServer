package app.joycourse.www.prod.service;

import app.joycourse.www.prod.dto.CourseInfoDto;
import app.joycourse.www.prod.dto.CourseListDto;
import app.joycourse.www.prod.exception.CustomException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.index.Settings;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class CourseElasticsearchService {
    private final ElasticsearchOperations es;
    @Value("classpath:courseDocumentMappings.json")
    Resource mapping;
    @Value("classpath:courseDocumentSettings.json")
    Resource setting;

    private boolean createIndex(IndexCoordinates indexCoordinates) throws IOException {
        IndexOperations indexOperations = es.indexOps(indexCoordinates);
        Settings settings = Settings.parse(IOUtils.toString(setting.getInputStream()));
        Document mappings = Document.parse(IOUtils.toString(mapping.getInputStream()));
        boolean result = indexOperations.create(settings, mappings);
        indexOperations.refresh();
        return result;
    }

    public String save(CourseInfoDto course) throws IOException {
        IndexCoordinates indexCoordinates = es.getIndexCoordinatesFor(CourseInfoDto.class);
        IndexOperations indexOperations = es.indexOps(indexCoordinates);
        if (!indexOperations.exists()) {
            if (!createIndex(indexCoordinates)) {
                throw new CustomException(CustomException.CustomError.SERVER_ERROR);
            }
        }
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(course.getId().toString())
                .withObject(course)
                .build();
        return es.index(indexQuery, indexCoordinates);
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
        if (courseHits.isEmpty()) {
            courseHits = new ArrayList<>();
        }
        return new CourseListDto(
                courseHits.size() < size,
                courseHits.size(),
                page,
                courseHits
        );
    }

    private QueryBuilder buildSearchQuery(String query) {
        return QueryBuilders.boolQuery()
                .must(QueryBuilders.boolQuery()
                                .should(QueryBuilders.matchQuery("title", query)).boost(35.0f)
                                .should(QueryBuilders.matchQuery("content", query)).boost(20.0f)
                                .should(QueryBuilders.matchQuery("userNickname", query)).boost(3.0f)
                                .should(QueryBuilders.matchQuery("courseDetailDtoList.place.addressName", query)).boost(15.0f)
                                .should(QueryBuilders.matchQuery("courseDetailDtoList.place.placeName", query)).boost(15.0f)
                                //.should(QueryBuilders.prefixQuery("courseDetailDtoList.place.addressName", query)).boost(15.0f)
                                .should(QueryBuilders.matchQuery("courseDetailDtoList.content", query)).boost(6.0f)
                        //.should(QueryBuilders.wildcardQuery("courseDetailDtoList.content", "*" + query + "*")).boost(6.0f)
                );
    }

}
