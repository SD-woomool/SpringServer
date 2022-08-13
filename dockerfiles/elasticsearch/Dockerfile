FROM docker.elastic.co/elasticsearch/elasticsearch:7.17.3

ARG PASSWORD

ENV ELASTIC_PASSWORD $PASSWORD

ENV xpack.security.enabled true

RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install --batch analysis-nori

EXPOSE 9200
