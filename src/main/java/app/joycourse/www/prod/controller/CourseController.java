package app.joycourse.www.prod.controller;

import app.joycourse.www.prod.domain.Course;
import app.joycourse.www.prod.domain.CourseDetail;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.dto.CourseSaveDto;
import app.joycourse.www.prod.dto.MyCourseListDto;
import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.service.CourseService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService){
        this.courseService = courseService;
    }

    @PostMapping("/")
    @ResponseBody
    public Response<CourseSaveDto> saveCourse(
            @RequestBody Course course,
            HttpServletRequest request
    ){  // 예외처리를 하나도 안함.
        Optional<User> optionalUser = Optional.ofNullable((User)request.getAttribute("user"));
        User user = optionalUser.orElseThrow(() -> new CustomException("NO_USER", CustomException.CustomError.MISSING_PARAMETERS));

        Course newCourse =  courseService.saveCourse(user, course);

        CourseSaveDto courseSaveDto = new CourseSaveDto(true, newCourse.getTitle(), newCourse.getContent(),
                newCourse.getLikeCnt(), newCourse.getTotalPrice(), newCourse.getCourseDetail());
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
    public Response<MyCourseListDto> getMyCourse(  // page, pageLength 없는경우 아직 해결 안됌
            @RequestParam(name = "page", defaultValue = "1") int page, // 이거 1 이하일때 오류 처리 하자
            @RequestParam(name = "page-length", defaultValue = "5") int pageLength,
            HttpServletRequest request
    ){
        User user  = Optional.ofNullable((User)request.getAttribute("user")).orElseThrow(() ->
                new CustomException("NO_USER", CustomException.CustomError.MISSING_PARAMETERS));
        return new Response<MyCourseListDto>(courseService.pagingMyCourse(user, page, pageLength));
    }



}
