package app.joycourse.www.prod.service;

import app.joycourse.www.prod.config.KakaoApiClient;
import app.joycourse.www.prod.config.PlaceRequestConfig;
import app.joycourse.www.prod.dto.PlaceSearchResponseDto;
import app.joycourse.www.prod.entity.CourseDetail;
import app.joycourse.www.prod.entity.Place;
import app.joycourse.www.prod.repository.PlaceCacheRepository;
import app.joycourse.www.prod.repository.PlaceRepository;
import app.joycourse.www.prod.repository.RedisPlaceCacheRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
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


    public Optional<PlaceSearchResponseDto> getPlaceByCache(String key) {
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
