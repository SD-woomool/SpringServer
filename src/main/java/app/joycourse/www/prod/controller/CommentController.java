package app.joycourse.www.prod.controller;


import app.joycourse.www.prod.domain.Comment;
import app.joycourse.www.prod.domain.Course;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.dto.CommentInfoDto;
import app.joycourse.www.prod.dto.CommentSaveDto;
import app.joycourse.www.prod.dto.CommentSaveRequestBodyDto;
import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.service.CommentService;
import app.joycourse.www.prod.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CourseService courseService;

    @PostMapping("/")
    @ResponseBody
    public Response<CommentSaveDto> saveComment(
            @RequestBody CommentSaveRequestBodyDto commentInfo,
            HttpServletRequest request
    ) {
        User user = Optional.ofNullable((User) request.getAttribute("user")).orElseThrow(() ->
                new CustomException("NO_USER", CustomException.CustomError.MISSING_PARAMETERS));

        Course course = courseService.getCourse(commentInfo.getCourseId());
        Comment savedComment = commentService.saveComment(commentInfo.getComment(), user, course);
        return new Response<CommentSaveDto>(new CommentSaveDto(true, new CommentInfoDto(savedComment)));
    }

}
