#FROM elasticsearch:8.3.1
FROM docker.elastic.co/elasticsearch/elasticsearch:8.3.1
# 위에 다운로드 안돼서 일단 docker pull롤 해놓은 상태

RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install --batch analysis-nori

EXPOSE 9200