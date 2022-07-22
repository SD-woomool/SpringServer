package app.joycourse.www.prod.service;

import app.joycourse.www.prod.dto.CourseDetailDto;
import app.joycourse.www.prod.dto.CourseInfoDto;
import app.joycourse.www.prod.dto.CourseListDto;
import app.joycourse.www.prod.dto.PlaceInfoDto;
import app.joycourse.www.prod.entity.Course;
import app.joycourse.www.prod.entity.CourseDetail;
import app.joycourse.www.prod.entity.Place;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.repository.CourseRepository;
import app.joycourse.www.prod.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseElasticsearchService courseElasticsearchService;
    private final PlaceRepository placeRepository;
    private final FileService fileService;

    @Transactional
    public Course saveCourse(User user, CourseInfoDto courseInfo, List<MultipartFile> files) {
        Course course = Course.of(courseInfo, user, null, null);
        if (files != null) {
            matchFiles(files, courseInfo.getCourseDetailDtoList());
        }
        List<CourseDetail> courseDetails = getCourseDetailList(courseInfo.getCourseDetailDtoList(), course);
        course.setCourseDetail(courseDetails);
        course.setTotalPrice();
        return courseRepository.saveCourse(course);
    }


    private void matchFiles(List<MultipartFile> files, List<CourseDetailDto> courseDetailDtos) {
        Map<String, String> fileUrlMap = fileService.uploadFiles(files, FileService.ImageFileType.COURSE_DETAIL_IMAGE);
        courseDetailDtos.stream()
                .filter((detail) -> detail.getPhoto() != null)
                .filter(detail -> detail.getPhoto().getFileName() != null)
                .forEach((detail) -> {
                    String newFileUrl = Optional.ofNullable(fileUrlMap.get(detail.getPhoto().getFileName())).orElseThrow(() -> new CustomException(CustomException.CustomError.SERVER_ERROR));
                    detail.getPhoto().setFileUrl(newFileUrl);
                });
    }

    private List<CourseDetail> getCourseDetailList(List<CourseDetailDto> courseDetailDtoList, Course course) {
        return courseDetailDtoList.stream().filter(Objects::nonNull).map(detailDto -> {
            PlaceInfoDto placeDto = detailDto.getPlace();

            if (placeDto == null) {
                throw new CustomException(CustomException.CustomError.MISSING_PARAMETERS);
            }

            Place place = placeRepository.findById(placeDto.getId()).orElseThrow(() -> new CustomException(CustomException.CustomError.INVALID_PARAMETER));
            return CourseDetail.of(detailDto, course, place);
        }).collect(Collectors.toList());
    }

    @Transactional
    public CourseListDto pagingCourse(int pageLength, int page) {
        List<CourseInfoDto> courseInfoList = new ArrayList<>();
        courseRepository.pagingById(pageLength, page).stream().flatMap(Collection::stream)
                .forEach((course) ->
                        courseInfoList.add(new CourseInfoDto(course))
                );
        return new CourseListDto(
                courseInfoList.size() < pageLength,
                pageLength,
                page,
                courseInfoList
        );
    }

    @Transactional
    public CourseListDto pagingMyCourse(User user, int pageLength, int page) {
        List<CourseInfoDto> courseInfoList = new ArrayList<>();
        courseRepository.pagingByUser(user, pageLength, page).stream().flatMap(Collection::stream)
                .forEach((course) ->
                        courseInfoList.add(new CourseInfoDto(course))
                );
        return new CourseListDto(
                courseInfoList.size() < pageLength,
                pageLength,
                page,
                courseInfoList
        );
    }

    @Transactional
    public void deleteCourse(User user, long courseId) {
        Course deleteCourse = courseRepository.findById(courseId).orElse(null);
        if (deleteCourse == null || !deleteCourse.getUser().getUid().equals(user.getUid())) {
            throw new CustomException(CustomException.CustomError.INVALID_PARAMETER);
        }
        CourseInfoDto deleteCourseInfoDto = new CourseInfoDto(deleteCourse);
        courseElasticsearchService.delete(deleteCourseInfoDto);
        courseRepository.deleteCourse(deleteCourse);
        deleteDeletedCourseFile(deleteCourseInfoDto.getCourseDetailDtoList());
    }

    private void deleteDeletedCourseFile(List<CourseDetailDto> courseDetailDtos) {
        courseDetailDtos.stream()
                .map(CourseDetailDto::getPhoto)
                .filter(photoInfo -> photoInfo.getFileUrl() != null)
                .forEach(photoInfo -> photoInfo.setDeleted(true));
        deleteUnmatchedFile(courseDetailDtos);
    }

    @Transactional
    public Course getCourse(Long courseId) throws CustomException {
        return courseRepository.findById(courseId).orElseThrow(() ->
                new CustomException(CustomException.CustomError.INVALID_PARAMETER));
    }


    @Transactional
    public void updateCourse(User user, Course course, CourseInfoDto newCourseInfo, List<MultipartFile> files) {
        Course newCourse = Course.of(newCourseInfo, user, null, null);
        deleteUnmatchedFile(newCourseInfo.getCourseDetailDtoList());
        if (files != null) {
            matchFiles(files, newCourseInfo.getCourseDetailDtoList());
        }
        List<CourseDetail> courseDetails = getCourseDetailList(newCourseInfo.getCourseDetailDtoList(), newCourse);
        newCourse.setCourseDetail(courseDetails);
        newCourse.setTotalPrice();
        newCourse.setLikeCnt(course.getLikeCnt());
        newCourse.setComments(course.getComments());
        courseRepository.mergeCourse(newCourse);
    }

    private void deleteUnmatchedFile(List<CourseDetailDto> courseDetailDtoList) {
        courseDetailDtoList.stream().map(CourseDetailDto::getPhoto)
                .filter(Objects::nonNull)
                .filter(photoInfo -> photoInfo.getFileUrl() != null && (photoInfo.getDeleted() || photoInfo.getFileName() != null))
                .forEach((photoInfo) -> {
                    String fileName = photoInfo.getFileUrl().split("files/")[1];
                    if (!fileService.deleteFile(fileName, FileService.ImageFileType.COURSE_DETAIL_IMAGE)) {
                        throw new CustomException(CustomException.CustomError.SERVER_ERROR);
                    }
                    photoInfo.setFileUrl(null);
                });
    }

}
