package app.joycourse.www.prod.controller;

import app.joycourse.www.prod.annotation.AuthorizationUser;
import app.joycourse.www.prod.dto.*;
import app.joycourse.www.prod.entity.Course;
import app.joycourse.www.prod.entity.Place;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.service.CourseService;
import app.joycourse.www.prod.service.FileService;
import app.joycourse.www.prod.service.PlaceService;
import app.joycourse.www.prod.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;
    private final PlaceService placeService;
    private final FileService fileService;
    private final ObjectMapper objectMapper;


    @GetMapping("/{course-id}/")
    @ResponseBody
    public Response<CourseInfoDto> getCourse(
            @PathVariable("course-id") Long courseId
    ) {
        Course findCourse = courseService.getCourse(courseId);
        return new Response<>(new CourseInfoDto(
                findCourse.getId(),
                findCourse.getUser().getNickname(),
                findCourse.getTitle(),
                findCourse.getContent(),
                findCourse.getLocation(),
                findCourse.getThumbnailUrl(),
                findCourse.getLikeCnt(),
                findCourse.getTotalPrice(),
                findCourse.getMemo(),
                findCourse.getCourseDetail()
        ));
    }


    /*
     * 제일 높은 pk를 알아내서 페이징
     */
    @GetMapping("/")
    @ResponseBody
    public Response<CourseListDto> getCourseList(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "page-length", defaultValue = "5") int pageLength
    ) {
        page = page < 1 ? 1 : page;
        pageLength = pageLength < 1 ? 5 : pageLength;
        return new Response<>(courseService.pagingCourse(pageLength, page));
    }

/*
    @PostMapping("/")
    @ResponseBody
    public Response<CourseSaveDto> saveCourse(@AuthorizationUser User user, @RequestBody CourseInfoDto courseInfo) {
        Course newCourse = courseService.saveCourse(user, courseInfo);
        CourseInfoDto courseInfoDto = new CourseInfoDto(
                newCourse.getId(),
                newCourse.getUser().getNickname(),
                newCourse.getTitle(),
                newCourse.getContent(),
                newCourse.getLocation(),
                newCourse.getThumbnailUrl(),
                newCourse.getLikeCnt(),
                newCourse.getTotalPrice(),
                newCourse.getMemo(),
                newCourse.getCourseDetail()
        );

        CourseSaveDto courseSaveDto = new CourseSaveDto(true, courseInfoDto);
        return new Response<>(courseSaveDto);
    }*/

    /*
     * save sevice에서 파일저장도 해야함
     * 그리고 file controller 주소 photo에 넣어서 주기
     * file controller httpservletresponse 에 파일넣어서 하기
     */
    @PostMapping(path = "new", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public Response<CourseSaveDto> saveCourseFormData(
            @AuthorizationUser User user,
            @RequestParam("body") String jsonBody,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            HttpServletResponse response
    ) throws JsonProcessingException {

        CourseInfoDto courseInfo = objectMapper.readValue(jsonBody, CourseInfoDto.class);
        Course newCourse = courseService.saveCourse(user, courseInfo, files);
        CourseInfoDto courseInfoDto = new CourseInfoDto(
                newCourse.getId(),
                newCourse.getUser().getNickname(),
                newCourse.getTitle(),
                newCourse.getContent(),
                newCourse.getLocation(),
                newCourse.getThumbnailUrl(),
                newCourse.getLikeCnt(),
                newCourse.getTotalPrice(),
                newCourse.getMemo(),
                newCourse.getCourseDetail()
        );

        CourseSaveDto courseSaveDto = new CourseSaveDto(true, courseInfoDto);
        return new Response<>(courseSaveDto);
    }

    /*
     * 일단 유저확인 o
     * 유져로 계시글 찾기
     * 근데 몇개씩 찾을지 정해야함
     * 중요한건 쿼리를 몇개씩 찾아오는게 가능한지, 가능하면 어떻게 해야하는지?
     * paging해야함 -> 시작 인덱스, 가져올 갯수, isEnd, 지금 몇번째 페이지 인지등 알면 될듯?
     */

    @GetMapping("/my-course")
    @ResponseBody
    public Response<CourseListDto> getMyCourseList(  // page, pageLength 없는경우 아직 해결 안됌
                                                     @RequestParam(name = "page", defaultValue = "1") int page,
                                                     @RequestParam(name = "page-length", defaultValue = "5") int pageLength,
                                                     @AuthorizationUser User user
    ) {
        page = page < 1 ? 1 : page;
        pageLength = pageLength < 1 ? 5 : pageLength;
        return new Response<>(courseService.pagingMyCourse(user, pageLength, page));
    }

    @DeleteMapping("/")
    @ResponseBody
    public Response<DeleteCourseDto> deleteCourse(
            @RequestParam(name = "course_id") long id,
            @AuthorizationUser User user
    ) {
        courseService.deleteCourse(user, id);

        return new Response<>(new DeleteCourseDto(true, id));
    }

    @PutMapping("/")
    @ResponseBody
    public Response<CourseInfoDto> editCourse(
            @RequestBody CourseInfoDto courseInfo, // 여기 dto로 바꾸자
            @AuthorizationUser User user
    ) {
        Course course = courseService.getCourse(courseInfo.getId());
        if (!course.getUser().getUid().equals(user.getUid()) || !course.getId().equals(courseInfo.getId())) {
            throw new CustomException(CustomException.CustomError.INVALID_PARAMETER);
        }
        courseService.updateCourse(course, courseInfo);

        return new Response<>(new CourseInfoDto(course));
    }

    @GetMapping("/place")
    @ResponseBody
    public Response<PlaceSearchResponseDto> getPlace(
            @AuthorizationUser User user,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "15") int size,
            @RequestParam(name = "query", defaultValue = "") String query,
            @RequestParam(name = "category_group_code", defaultValue = "") String categoryGroupCode  // 태그를 어떻게 처리하지?
    ) {
        if (query == null || query.length() == 0) {
            throw new CustomException(CustomException.CustomError.MISSING_PARAMETERS);
        }
        String key = query + "_" + page + "_" + size + "_" + categoryGroupCode;
        PlaceSearchResponseDto places = placeService.getPlaceByCache(key).orElseGet(() -> {
            try {
                System.out.println("response kakao api data");
                PlaceSearchResponseDto placeSearchResponse = placeService.getPlaceByFeign(
                        query,
                        page,
                        size,
                        categoryGroupCode
                ).orElseThrow();
                placeSearchResponse.getDocuments().stream().filter(Objects::nonNull).forEach((placeInfo) -> {
                    Place place = new Place(
                            null, placeInfo.getX(), placeInfo.getY(), placeInfo.getPlaceName(),
                            placeInfo.getCategoryName(), placeInfo.getCategoryGroupCode(), placeInfo.getCategoryGroupName(),
                            placeInfo.getPhone(), placeInfo.getAddressName(), placeInfo.getRoadAddressName(), placeInfo.getPlaceUrl(),
                            placeInfo.getDistance(), null
                    );
                    placeService.savePlace(place, null);
                    placeInfo.setId(place.getId());
                });
                placeService.cachePlace(key, placeSearchResponse);
                return placeSearchResponse;
            } catch (URISyntaxException | JsonProcessingException e) {
                e.printStackTrace();
                throw new CustomException(CustomException.CustomError.SERVER_ERROR);
            }
        });
        return new Response<>(places);
    }

}
