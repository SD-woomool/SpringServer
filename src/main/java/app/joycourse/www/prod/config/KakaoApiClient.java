package app.joycourse.www.prod.config;

import app.joycourse.www.prod.dto.PlaceSearchResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@FeignClient(
        name = "kakao-api-client",
        url = "https://dapi.kakao.com") // 이거 바꿔
public interface KakaoApiClient {
    @RequestMapping(
            method = RequestMethod.GET,
            value = "/v2/local/search/keyword.json",
            produces = "application/json"
    )
    PlaceSearchResponseDto requestPlace(
            URI requestUri,
            @RequestHeader("Authorization") String apiKey,
            @RequestParam(name = "query") String query,
            @RequestParam(name = "page") int page,
            @RequestParam(name = "size") int size,
            @RequestParam(name = "analyze_type") String analyzeType,
            @RequestParam(name = "category_group_code") String categoryGroupCode
    );
}
