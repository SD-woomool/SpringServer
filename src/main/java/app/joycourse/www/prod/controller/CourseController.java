package app.joycourse.www.prod.controller;

import app.joycourse.www.prod.domain.Course;
import app.joycourse.www.prod.domain.CourseDetail;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.dto.CourseSaveDto;
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
    public Response<CourseSaveDto> saveCourse(@RequestBody Course course, HttpServletRequest request){  // 예외처리를 하나도 안함.
        Optional<User> optionalUser = Optional.ofNullable((User)request.getAttribute("user"));
        System.out.println("*************"+optionalUser);
        User user = optionalUser.orElseThrow(() -> new CustomException("NO_USER", CustomException.CustomError.MISSING_PARAMETERS));

        Course newCourse =  courseService.saveCourse(user, course);

        CourseSaveDto courseSaveDto = new CourseSaveDto(true, newCourse.getTitle(), newCourse.getContent(),
                newCourse.getLikeCnt(), newCourse.getTotalPrice(), newCourse.getCourseDetail());
        return new Response<CourseSaveDto>(courseSaveDto);
    }

}
