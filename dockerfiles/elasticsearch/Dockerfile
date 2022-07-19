#FROM elasticsearch:8.3.1
FROM docker.elastic.co/elasticsearch/elasticsearch:7.17.3

RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install --batch analysis-nori

EXPOSE 9200