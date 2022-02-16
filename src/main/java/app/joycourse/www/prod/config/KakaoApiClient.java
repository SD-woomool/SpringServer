package app.joycourse.www.prod.config;

import app.joycourse.www.prod.dto.PlaceSearchResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "kakao-api-client",
        url = "${place-api.request-parameter.request-uri}"
)
public interface KakaoApiClient {
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/v2/local/search/keyword.json",
            produces = "application/json"
    )
    PlaceSearchResponseDto requestPlace(
            @RequestHeader("Authorization") String apiKey,
            @RequestParam(name = "query") String query,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size,
            @RequestParam(name = "analyze_type") String analyzeType,
            @RequestParam(name = "category_group_code") String categoryGroupCode
    );
}
