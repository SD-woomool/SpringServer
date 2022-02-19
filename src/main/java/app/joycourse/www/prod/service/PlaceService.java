package app.joycourse.www.prod.service;

import app.joycourse.www.prod.config.KakaoApiClient;
import app.joycourse.www.prod.config.PlaceRequestConfig;
import app.joycourse.www.prod.domain.CourseDetail;
import app.joycourse.www.prod.domain.Place;
import app.joycourse.www.prod.dto.PlaceSearchResponseDto;
import app.joycourse.www.prod.repository.PlaceCacheRepository;
import app.joycourse.www.prod.repository.PlaceRepository;
import app.joycourse.www.prod.repository.RedisPlaceCacheRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRequestConfig placeRequestConfig;
    private final RestTemplate restTemplate;
    private final PlaceRepository placeRepository;
    private final PlaceCacheRepository placeCacheRepository;
    private final RedisPlaceCacheRepository redisPlaceCacheRepository;
    private final KakaoApiClient kakaoApiClient;
    private final ObjectMapper objectMapper;
    private final StringRedisTemplate redisTemplate;

    /*
     * 사용안함
     */
    public PlaceSearchResponseDto getPlace(String query, int page, int size, String categoryGroupCode) throws UnsupportedEncodingException, IOException {
        try {
            Optional<String> cachedResponse = findCachedPlaceResponse(query);
            String placeResponse = cachedResponse.get();
            PlaceSearchResponseDto cachePlaceSearchResponseDto = objectMapper.readValue(placeResponse, PlaceSearchResponseDto.class);

            System.out.println("response cached data");
            return cachePlaceSearchResponseDto;
        } catch (Exception e) {
            System.out.println("no cached data");
            PlaceRequestConfig.RequestParameter requestParameter = placeRequestConfig.getRequestParameter();
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<?> httpEntity = new HttpEntity<>(headers);
            headers.add("Authorization", String.format("KakaoAK %s", requestParameter.getRestApiKey()));
            UriComponents uri = UriComponentsBuilder.fromHttpUrl(requestParameter.getRequestUri()).
                    queryParam("page", page).
                    queryParam("size", size).
                    queryParam("analyze_type", "similar").
                    queryParam("query", query).
                    queryParam("category_group_code", categoryGroupCode).build();
            PlaceSearchResponseDto placeSearchResponseDto = restTemplate.exchange(uri.toUri(), HttpMethod.GET, httpEntity, PlaceSearchResponseDto.class).getBody();
            assert placeSearchResponseDto != null;
            placeSearchResponseDto.getDocuments().stream().filter(Objects::nonNull).forEach((placeInfo) -> {
                Place place = new Place(
                        null, placeInfo.getX(), placeInfo.getY(), placeInfo.getPlaceName(),
                        placeInfo.getCategoryName(), placeInfo.getCategoryGroupCode(), placeInfo.getCategoryGroupName(),
                        placeInfo.getPhone(), placeInfo.getAddressName(), placeInfo.getRoadAddressName(), placeInfo.getPlaceUrl(),
                        placeInfo.getDistance(), null
                );
                savePlace(place, null);
                placeInfo.setId(place.getId());
            });
            savePlaceCache(query, objectMapper.writeValueAsString(placeSearchResponseDto));
            return placeSearchResponseDto;
        }
    }

    public Optional<PlaceSearchResponseDto> getPlaceByCache(String key) throws JsonProcessingException {
        /*try {
            ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
            String placeResponse = stringValueOperations.get(key);
            PlaceSearchResponseDto cachePlaceSearchResponse = objectMapper.readValue(placeResponse, PlaceSearchResponseDto.class);
            System.out.println("response cached data");
            return Optional.ofNullable(cachePlaceSearchResponse);
        } catch (JsonProcessingException | IllegalArgumentException e) {
            return Optional.empty();
        }*/
        Optional<PlaceSearchResponseDto> placeResponse = redisPlaceCacheRepository.findById(key);
        if (placeResponse.isPresent()) {
            System.out.println("response cached data");
        }
        return placeResponse;


    }

    public Optional<PlaceSearchResponseDto> getPlaceByFeign(String query, int page, int size, String categoryGroupCode) throws URISyntaxException, JsonProcessingException {

        PlaceSearchResponseDto placeResponse = kakaoApiClient.requestPlace(
                "KakaoAK " + placeRequestConfig.getRequestParameter().getRestApiKey(),
                query, page, size, "similar", categoryGroupCode
        );
        return Optional.ofNullable(placeResponse);
    }

    public void cachePlace(String key, PlaceSearchResponseDto placeInfo) throws JsonProcessingException {
        /*ValueOperations<String, String> stringValueOperations = redisTemplate.opsForValue();
        stringValueOperations.set(key, objectMapper.writeValueAsString(placeInfo));*/
        placeInfo.setId(key);
        redisPlaceCacheRepository.save(placeInfo);
    }

    public void savePlace(Place place, CourseDetail courseDetail) {
        place.setCourseDetails(courseDetail);
        placeRepository.save(place);
    }

    public Optional<String> findCachedPlaceResponse(String keyword) {
        return placeCacheRepository.findByKeyword(keyword);
    }

    public void savePlaceCache(String keyword, String placeString) {
        placeCacheRepository.save(keyword, placeString);
    }
}
