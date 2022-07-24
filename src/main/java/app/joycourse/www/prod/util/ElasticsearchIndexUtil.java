package app.joycourse.www.prod.util;

import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

public class ElasticsearchIndexUtil {

    public IndexCoordinates createIndexNameWrapper(String indexName) {
        return IndexCoordinates.of(indexName);
    }

    public IndexCoordinates createIndexWithPostFixWrapper(String indexName) {
        return IndexCoordinates.of(indexName + '-' + System.currentTimeMillis());
    }
}
