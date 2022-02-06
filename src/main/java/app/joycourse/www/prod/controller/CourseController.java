package app.joycourse.www.prod.controller;

import app.joycourse.www.prod.domain.Course;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.dto.*;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.service.CourseService;
import app.joycourse.www.prod.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;


@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final PlaceService placeService;


    @GetMapping("/{course-id}/")
    @ResponseBody
    public Response<CourseInfoDto> getCourse(
            @PathVariable("course-id") Long courseId
    ) {
        Course findCourse = courseService.getCourse(courseId);
        return new Response<CourseInfoDto>(new CourseInfoDto(
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
        return new Response<CourseListDto>(courseService.pagingCourse(pageLength, page));
    }


    @PostMapping("/")
    @ResponseBody
    public Response<CourseSaveDto> saveCourse(
            @RequestBody Course course,
            HttpServletRequest request
    ) {  // 예외처리를 하나도 안함.
        Optional<User> optionalUser = Optional.ofNullable((User) request.getAttribute("user"));
        User user = optionalUser.orElseThrow(() -> new CustomException("NO_USER", CustomException.CustomError.MISSING_PARAMETERS));

        Course newCourse = courseService.saveCourse(user, course);
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
        return new Response<CourseSaveDto>(courseSaveDto);
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
                                                     HttpServletRequest request
    ) {
        User user = Optional.ofNullable((User) request.getAttribute("user")).orElseThrow(() ->
                new CustomException("NO_USER", CustomException.CustomError.MISSING_PARAMETERS));
        page = page < 1 ? 1 : page;
        pageLength = pageLength < 1 ? 5 : pageLength;
        return new Response<CourseListDto>(courseService.pagingMyCourse(user, pageLength, page));
    }

    @DeleteMapping("/")
    @ResponseBody
    public Response<DeleteCourseDto> deleteCourse(
            @RequestParam(name = "course_id") long id,
            HttpServletRequest request
    ) {
        User user = Optional.ofNullable((User) request.getAttribute("user")).orElseThrow(() ->
                new CustomException("NO_USER", CustomException.CustomError.MISSING_PARAMETERS));
        courseService.deleteCourse(user, id);

        return new Response<DeleteCourseDto>(new DeleteCourseDto(true, id));
    }

    @PutMapping("/")
    @ResponseBody
    public Response<CourseInfoDto> editCourse(
            @RequestBody Course courseInfo,
            HttpServletRequest request
    ) {
        User user = Optional.ofNullable((User) request.getAttribute("user")).orElseThrow(() ->
                new CustomException("NO_USER", CustomException.CustomError.MISSING_PARAMETERS));

        Course course = courseService.getCourse(courseInfo.getId());
        if (course.equals(courseInfo) || !course.getUser().getId().equals(user.getId())) {
            throw new CustomException("INVALID_COURSE_INFO", CustomException.CustomError.INVALID_PARAMETER);
        }
        courseService.updateCourse(course, courseInfo);

        return new Response<CourseInfoDto>(new CourseInfoDto(course));
    }

    @GetMapping("/place")
    @ResponseBody
    public Response<PlaceSearchResponseDto> getPlace(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "15") int size,
            @RequestParam(name = "query") String query,
            @RequestParam(name = "category_group_code") String categoryGroupCode  // 태그를 어떻게 처리하지?
    ) throws UnsupportedEncodingException, IOException {
        // 여기서 일단 db조회해서 찾아보고 없으면 검색
        PlaceSearchResponseDto places = placeService.getPlace(query, page, size, categoryGroupCode);


        return new Response<PlaceSearchResponseDto>(places);
    }

}
