package app.joycourse.www.prod.config;


/*
@Configuration
@ConfigurationProperties("elasticsearch")
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {
    private String host;
    private int port;

    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchOperations() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }
}*/
