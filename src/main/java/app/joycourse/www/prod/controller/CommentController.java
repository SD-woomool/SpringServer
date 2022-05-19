package app.joycourse.www.prod.controller;


import app.joycourse.www.prod.annotation.AuthorizationUser;
import app.joycourse.www.prod.dto.CommentDeleteDto;
import app.joycourse.www.prod.dto.CommentInfoDto;
import app.joycourse.www.prod.dto.CommentListDto;
import app.joycourse.www.prod.dto.CommentRequestBodyDto;
import app.joycourse.www.prod.dto.CommentSaveDto;
import app.joycourse.www.prod.dto.CommentUpdateDto;
import app.joycourse.www.prod.dto.Response;
import app.joycourse.www.prod.entity.Comment;
import app.joycourse.www.prod.entity.Course;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.service.CommentService;
import app.joycourse.www.prod.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Api(tags = "댓글")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CourseService courseService;

    @ApiOperation(
            value = "댓글을 작성한다.",
            notes = "특정 글에 댓글을 작성한다.\n",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @PostMapping
    public Response<CommentSaveDto> saveComment(
            @RequestBody CommentRequestBodyDto commentInfo,
            @AuthorizationUser User user
    ) {
        Course course = courseService.getCourse(commentInfo.getCourseId());
        Comment comment = new Comment(commentInfo.getCommentInfo());
        Comment savedComment = commentService.saveComment(comment, user, course);
        return new Response<>(new CommentSaveDto(true, new CommentInfoDto(savedComment)));
    }

    @ApiOperation(
            value = "댓글 목록을 가져온다.",
            notes = "특정 글에 댓글 목록을 가져온다.\n",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @GetMapping
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
                .forEach((comment) ->
                        commentList.add(new CommentInfoDto(comment))
                );
        return new Response<>(new CommentListDto(
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
    @ApiOperation(
            value = "댓글을 삭제한다",
            notes = "특정 댓글을 삭제한다.\n",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @DeleteMapping
    public Response<CommentDeleteDto> deleteComment(
            @RequestParam(name = "comment_id") Long commentId,
            @AuthorizationUser User user
    ) {
        Comment targetComment = commentService.findComment(commentId);
        int deleteCnt = commentService.deleteCommentsByParentId(targetComment.getId());
        commentService.deleteComments(targetComment);
        deleteCnt++;

        return new Response<>(new CommentDeleteDto(true, deleteCnt));
    }

    @ApiOperation(
            value = "댓글을 수정한다.",
            notes = "특정 댓글을 수정한다.\n",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @PutMapping
    public Response<CommentUpdateDto> editComment(
            @RequestBody CommentRequestBodyDto commentInfo,
            @AuthorizationUser User user
    ) {
        Comment comment = commentService.findComment(commentInfo.getCommentInfo().getId());
        Comment newComment = new Comment(commentInfo.getCommentInfo());
        Comment updatedComment = commentService.updateComment(comment, newComment, user);
        return new Response<>(new CommentUpdateDto(true, new CommentInfoDto(updatedComment)));
    }
}
