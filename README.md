# JoyCourse Spring Server

[JoyCourse](https://www.joycourse.app)

## Project 관리

https://github.com/SD-woomool/SpringServer/projects/1

## Architecture

--- 

## Git Convention

### commit message

```
feat : 새로운 기능에 대한 커밋
fix : 버그 수정에 대한 커밋
build : 빌드 관련 파일 수정에 대한 커밋
chore : 그 외 자잘한 수정에 대한 커밋
ci : CI관련 설정 수정에 대한 커밋
docs : 문서 수정에 대한 커밋
style : 코드 스타일 혹은 포맷 등에 관한 커밋
refactor :  코드 리팩토링에 대한 커밋
test : 테스트 코드 수정에 대한 커밋

예시: feat(login): login controller

```

참조: [https://beomseok95.tistory.com/328](https://beomseok95.tistory.com/328)

### branch

```
0.1.0: release branch
main: dev에서 합쳐진것이다.
dev: feature에서 개발한것을 합친다.
feature/login: login 기능에 대한 브랜치
```

---

### Docker 환경 설정법

#### MySQL 8.0

```shell
# docker build, image의 tag를 joycourse-mysql
$ docker build dockerfiles/mysql --tag joycourse-mysql

# docker 실행, 컨테이너 이름은 joycourse-mysql, 볼륨을 실행 피시의 ~/mysql에 마운트한다.
$ docker run -d -p 3306:3306 --name joycourse-mysql -v ~/mysql:/var/lib/mysql joycourse-mysql --character-set-server=utf8mb4 --collation-server=utf8mb4_unicode_ci

# docker mysql 접속 방법, 비밀번호는 joycourse
$ docker exec -it joycourse-mysql mysql -uroot -p 
```

#### Redis 6.2.6-alpine

```shell
# docker build, image의 tag를 joycourse-redis
$ docker build dockerfiles/redis --tag joycourse-redis

# docker joycourse-redis로 실행
$ docker run -d -p 6379:6379 --name joycourse-redis -v ~/redis:/data joycourse-redis redis-server --save 60 1

# docker redis 접속 방법
$ docker exec -it joycourse-redis redis-cli
```

#### Elasticsearch

```shell
# docker build, image의 tag를 joycourse-elasticsearch
$ docker build dockerfiles/elasticsearch --tag joycourse-elasticsearch

# docker joycourse-elasticsearch로 실행
$ docker run -d -p 9200:9200 -p 9300:9300 --name joycourse-elasticsearch -v elasticsearch:/usr/share/elasticsearch/data -e "discovery.type=single-node" joycourse-elasticsearch

```

#### Spring

```shell
$ docker build . --tag joycourse-spring
$ docker run -d -p 8080:8080 --name joycourse-spring joycourse-spring
# 스프링 로그 조회하기
$ docker logs joycourse-spring 
```

#### check jenkins 15
