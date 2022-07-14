package app.joycourse.www.prod.controller;

import app.joycourse.www.prod.annotation.AuthorizationUser;
import app.joycourse.www.prod.dto.*;
import app.joycourse.www.prod.entity.Course;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URISyntaxException;
import java.util.List;


@Api(tags = "코스")
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseElasticsearchService courseElasticsearch;
    private final UserService userService;
    private final PlaceService placeService;
    private final FileService fileService;
    private final ObjectMapper objectMapper;


    @ApiOperation(
            value = "코스 상세 정보를 가져온다.",
            notes = "코스 상세 정보를 가져온다.\n",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @GetMapping("/{course-id}/")
    public Response<CourseInfoDto> getCourse(
            @PathVariable("course-id") Long courseId
    ) {
        Course findCourse = courseService.getCourse(courseId);
        return new Response<>(new CourseInfoDto(findCourse));
    }


    /*
     * 제일 높은 pk를 알아내서 페이징
     */
    @ApiOperation(
            value = "코스 목록을 가져온다.",
            notes = "코스 목록을 가져온다.\n",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @GetMapping
    public Response<CourseListDto> getCourseList(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "page-length", defaultValue = "5") int pageLength
    ) {
        page = page < 1 ? 1 : page;
        pageLength = pageLength < 1 ? 5 : pageLength;
        return new Response<>(courseService.pagingCourse(pageLength, page));
    }


    @ApiOperation(
            value = "코스를 작성한다.",
            notes = "코스를 작성한다.\n",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @PostMapping
    public Response<CourseSaveDto> saveCourseFormData(
            @AuthorizationUser User user,
            @Valid @RequestPart(value = "body") CourseInfoDto courseInfo,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {

        Course newCourse = courseService.saveCourse(user, courseInfo, files);

        CourseInfoDto courseInfoDto = new CourseInfoDto(newCourse);
        CourseSaveDto courseSaveDto = new CourseSaveDto(true, courseInfoDto);
        courseElasticsearch.save(courseInfoDto);
        return new Response<>(courseSaveDto);
    }


    @ApiOperation(
            value = "내가 작성한 코스 목록을 가져온다.",
            notes = "내가 작성한 코스 목록을 가져온다.\n",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @GetMapping("/my-course")
    public Response<CourseListDto> getMyCourseList(  // page, pageLength 없는경우 아직 해결 안됌
                                                     @RequestParam(name = "page", defaultValue = "1") int page,
                                                     @RequestParam(name = "page-length", defaultValue = "5") int pageLength,
                                                     @AuthorizationUser User user
    ) {
        page = page < 1 ? 1 : page;
        pageLength = pageLength < 1 ? 5 : pageLength;
        return new Response<>(courseService.pagingMyCourse(user, pageLength, page));
    }

    @ApiOperation(
            value = "코스를 삭제한다.",
            notes = "코스를 삭제한다.\n",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @DeleteMapping
    public Response<DeleteCourseDto> deleteCourse(
            @RequestParam(name = "course_id") long id,
            @AuthorizationUser User user
    ) {
        courseService.deleteCourse(user, id);

        return new Response<>(new DeleteCourseDto(true, id));
    }


    @ApiOperation(
            value = "코스를 수정한다.",
            notes = "코스를 수정한다.\n",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @PutMapping
    public Response<CourseInfoDto> editCourse(
            @Valid @RequestPart(value = "body") CourseInfoDto courseInfo,
            @AuthorizationUser User user,
            @RequestPart(value = "files", required = false) List<MultipartFile> files
    ) {
        Course course = courseService.getCourse(courseInfo.getId());
        if (!course.getUser().getUid().equals(user.getUid()) || !course.getId().equals(courseInfo.getId())) {
            throw new CustomException(CustomException.CustomError.INVALID_PARAMETER);
        }
        courseService.updateCourse(user, course, courseInfo, files);

        return new Response<>(new CourseInfoDto(course));
    }

    @ApiOperation(
            value = "장소 목록을 가져온다.",
            notes = "장소 목록을 가져온다.\n",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @GetMapping("/place")
    public Response<PlaceSearchResponseDto> getPlace(
            @AuthorizationUser User user,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "15") int size,
            @RequestParam(name = "query", defaultValue = "") String query,
            @RequestParam(name = "category_group_code", defaultValue = "") String categoryGroupCode  // 태그를 어떻게 처리하지?
    ) throws URISyntaxException, JsonProcessingException {
        if (query == null || query.length() == 0) {
            throw new CustomException(CustomException.CustomError.MISSING_PARAMETERS);
        }
        String key = query + "_" + page + "_" + size + "_" + categoryGroupCode;
        PlaceSearchResponseDto places = placeService.getPlace(key, query, page, size, categoryGroupCode);
        return new Response<>(places);
    }

    @GetMapping("/course_search")
    public Response<CourseListDto> searchCourse(
            @RequestParam(name = "query") String query,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size
    ) {
        CourseListDto courseList = courseElasticsearch.searchCourseByQuery(query, page, size);
        return new Response<>(courseList);
    }

}
