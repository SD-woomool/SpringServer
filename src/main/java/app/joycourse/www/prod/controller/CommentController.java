package app.joycourse.www.prod.controller;


import app.joycourse.www.prod.domain.Comment;
import app.joycourse.www.prod.domain.Course;
import app.joycourse.www.prod.domain.User;
import app.joycourse.www.prod.dto.*;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.service.CommentService;
import app.joycourse.www.prod.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CourseService courseService;

    @PostMapping("/")
    @ResponseBody
    public Response<CommentSaveDto> saveComment(
            @RequestBody CommentRequestBodyDto commentInfo,
            HttpServletRequest request
    ) {
        User user = Optional.ofNullable((User) request.getAttribute("user")).orElseThrow(() ->
                new CustomException("NO_USER", CustomException.CustomError.MISSING_PARAMETERS));

        Course course = courseService.getCourse(commentInfo.getCourseId());
        Comment comment = new Comment(commentInfo.getCommentInfo());
        Comment savedComment = commentService.saveComment(comment, user, course);
        return new Response<CommentSaveDto>(new CommentSaveDto(true, new CommentInfoDto(savedComment)));
    }


    @GetMapping("/")
    @ResponseBody
    public Response<CommentListDto> getCommentList(
            @RequestParam(name = "course_id") Long courseId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "page_length", defaultValue = "10") int pageLength
    ) {
        Course course = courseService.getCourse(courseId);
        page = page <= 0 ? 1 : page;
        pageLength = pageLength <= 0 ? 10 : pageLength;
        List<CommentInfoDto> commentList = new ArrayList<>();
        commentService.pagingComment(course, page, pageLength).stream().flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .forEach((comment) -> {
                    commentList.add(new CommentInfoDto(comment));
                });
        return new Response<CommentListDto>(new CommentListDto(
                commentList.size() < pageLength,
                page,
                pageLength,
                commentList
        ));
    }

    /*
     *자식 댓글이 있는경우 모두 삭제해야해
     * em은 bulk delete 지원 안함
     * 그냥 부모 댓글로 지우는 쿼리를 만들어서 지워야해
     * 지금은 레포에서 getresultlist를 지원 안하는듯 지울때는.... 다시해
     */
    @DeleteMapping("/")
    @ResponseBody
    public Response<CommentDeleteDto> deleteComment(
            @RequestParam(name = "comment_id") Long commentId,
            HttpServletRequest request
    ) {
        User user = Optional.ofNullable((User) request.getAttribute("user")).orElseThrow(() ->
                new CustomException("NO_USER", CustomException.CustomError.MISSING_PARAMETERS));

        Comment targetComment = commentService.findComment(commentId);
        int deleteCnt = commentService.deleteCommentsByParentId(targetComment.getId());
        commentService.deleteComments(targetComment);
        deleteCnt++;

        return new Response<CommentDeleteDto>(new CommentDeleteDto(true, deleteCnt));
    }

    @PutMapping("/")
    @ResponseBody
    public Response<CommentUpdateDto> editComment(
            @RequestBody CommentRequestBodyDto commentInfo,
            HttpServletRequest request
    ) {
        User user = Optional.ofNullable((User) request.getAttribute("user")).orElseThrow(() ->
                new CustomException("NO_USER", CustomException.CustomError.MISSING_PARAMETERS));

        Comment comment = commentService.findComment(commentInfo.getCommentInfo().getId());
        Comment newComment = new Comment(commentInfo.getCommentInfo());
        Comment updatedComment = commentService.updateComment(comment, newComment, user);
        return new Response<CommentUpdateDto>(new CommentUpdateDto(true, new CommentInfoDto(updatedComment)));
    }
}
