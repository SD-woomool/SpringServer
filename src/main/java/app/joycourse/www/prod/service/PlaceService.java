package app.joycourse.www.prod.service;

import app.joycourse.www.prod.config.PlaceRequestConfig;
import app.joycourse.www.prod.domain.CourseDetail;
import app.joycourse.www.prod.domain.Place;
import app.joycourse.www.prod.dto.PlaceSearchResponseDto;
import app.joycourse.www.prod.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRequestConfig placeRequestConfig;
    private final RestTemplate restTemplate;
    private final PlaceRepository placeRepository;

    public PlaceSearchResponseDto getPlace(String query, int page, int size) throws UnsupportedEncodingException {
        PlaceRequestConfig.RequestParameter requestParameter = placeRequestConfig.getRequestParameter();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        headers.add("Authorization", String.format("KakaoAK %s", requestParameter.getRestApiKey()));
        UriComponents uri = UriComponentsBuilder.fromHttpUrl(requestParameter.getRequestUri()).
                queryParam("page", page).
                queryParam("size", size).
                queryParam("analyze_type", "similar").
                queryParam("query", query).build();
        System.out.println("url: " + uri);
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
        return placeSearchResponseDto;
    }

    public void savePlace(Place place, CourseDetail courseDetail) {
        place.setCourseDetails(courseDetail);
        placeRepository.save(place);
    }
}
