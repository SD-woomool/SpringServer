package app.joycourse.www.prod.controller;

import app.joycourse.www.prod.domain.Course;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.dto.CourseSaveDto;
import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.service.CourseService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/Course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService){
        this.courseService = courseService;
    }


    @PostMapping("/")
    @ResponseBody
    public Response<CourseSaveDto> saveCourse(@RequestBody Course course, HttpServletRequest request){
        Optional<User> optionalUser = (Optional<User>) request.getAttribute("user");
        User user = optionalUser.orElse(null);
        Course newCourse =  courseService.saveCourse(user, course);
        CourseSaveDto courseSaveDto = new CourseSaveDto(true, newCourse.getTitle(), newCourse.getContent(),
                newCourse.getLikeCnt(), newCourse.getTotalPrice(), newCourse.getCourseDetail());
        return new Response<CourseSaveDto>(courseSaveDto);
    }
}
