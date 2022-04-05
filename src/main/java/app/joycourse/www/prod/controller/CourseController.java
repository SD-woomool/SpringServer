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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
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


    @PostMapping(path = "/")
    @ResponseBody
    public Response<CourseSaveDto> saveCourseFormData(
            @AuthorizationUser User user,
            @Valid @RequestPart(value = "body") CourseInfoDto courseInfo,
            @RequestPart(value = "files", required = false) List<MultipartFile> files,
            HttpServletResponse response
    ) throws JsonProcessingException {

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


    @PutMapping(value = "/")
    @ResponseBody
    public Response<CourseInfoDto> editCourse(
            @Valid @RequestPart(value = "body") CourseInfoDto courseInfo,
            @AuthorizationUser User user,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) throws JsonProcessingException {
        Course course = courseService.getCourse(courseInfo.getId());
        if (!course.getUser().getUid().equals(user.getUid()) || !course.getId().equals(courseInfo.getId())) {
            throw new CustomException(CustomException.CustomError.INVALID_PARAMETER);
        }
        courseService.updateCourse(course, courseInfo, files);

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
