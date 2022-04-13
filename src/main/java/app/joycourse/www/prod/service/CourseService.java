package app.joycourse.www.prod.service;

import app.joycourse.www.prod.dto.*;
import app.joycourse.www.prod.entity.Course;
import app.joycourse.www.prod.entity.CourseDetail;
import app.joycourse.www.prod.entity.Place;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.repository.CourseDetailRepository;
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
    private final CourseDetailRepository courseDetailRepository;
    private final PlaceRepository placeRepository;
    private final FileService fileService;

    @Transactional
    public Course saveCourse(User user, CourseInfoDto courseInfo, List<MultipartFile> files) {
        Course course = Course.of(courseInfo, user, null, null);
        List<CourseDetail> courseDetails = getCourseDetailList(courseInfo.getCourseDetailDtoList(), course);
        course.setCourseDetail(courseDetails);
        course.setTotalPrice();
        if (files != null) {
            matchFiles(files, courseDetails);
        }
        return courseRepository.saveCourse(course);
    }

    private void matchFiles(List<MultipartFile> files, List<CourseDetail> courseDetails) {
        Map<String, String> fileUrlMap = fileService.uploadFiles(files, FileService.ImageFileType.COURSE_DETAIL_IMAGE);
        courseDetails.stream()
                .filter((detail) -> detail.getPhoto() != null)
                .forEach((detail) -> {
                    String newFileUrl = Optional.ofNullable(fileUrlMap.get(detail.getPhoto())).orElseThrow(() -> new CustomException(CustomException.CustomError.SERVER_ERROR));
                    detail.setPhoto(newFileUrl);
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
        if (deleteCourse == null || deleteCourse.getUser() != user) {
            throw new CustomException(CustomException.CustomError.INVALID_PARAMETER);
        }
        courseRepository.deleteCourse(deleteCourse);
    }

    @Transactional
    public Course getCourse(Long courseId) throws CustomException {
        return courseRepository.findById(courseId).orElseThrow(() ->
                new CustomException(CustomException.CustomError.INVALID_PARAMETER));
    }


    @Transactional
    public void updateCourse(Course course, CourseInfoDto newCourseInfo, List<MultipartFile> files) {
        Course newCourse = new Course(newCourseInfo);
        newCourseInfo.getCourseDetailDtoList().stream().filter(Objects::nonNull).forEach((detailDto) -> {
            PlaceInfoDto placeDto = detailDto.getPlace();
            if (placeDto == null) {
                throw new CustomException(CustomException.CustomError.MISSING_PARAMETERS);
            }
            Place place = placeRepository.findById(placeDto.getId()).orElseThrow(() -> new CustomException(CustomException.CustomError.INVALID_PARAMETER));
            CourseDetail courseDetail = detailDto.convertToEntity();
            courseDetail.setPlace(place);
            courseDetail.setCourse(newCourse);
            newCourse.addCourseDetail(courseDetail);
            place.setCourseDetails(courseDetail);
            PhotoInfoDto photoInfo = Optional.ofNullable(detailDto.getPhoto()).orElse(new PhotoInfoDto());
            Boolean fileDeleted = Optional.ofNullable(photoInfo.getDeleted()).orElse(false);
            if (photoInfo.getFileUrl() != null && (photoInfo.getFileName() != null || fileDeleted)) { // 사진이 변경된 경우 기존 파일을 지우는 부분
                String fileName = photoInfo.getFileUrl().split("files/")[1];
                if (!fileService.deleteFile(fileName, FileService.ImageFileType.COURSE_DETAIL_IMAGE)) {
                    throw new CustomException(CustomException.CustomError.SERVER_ERROR);
                }
                String photoName = fileDeleted ? null : photoInfo.getFileName();
                courseDetail.setPhoto(photoName);
            }
        });
        newCourse.setUser(course.getUser());
        newCourse.setLikeCnt(course.getLikeCnt());
        if (files != null) {
            Map<String, String> fileUrlMap = fileService.uploadFiles(files, FileService.ImageFileType.COURSE_DETAIL_IMAGE);
            newCourse.getCourseDetailList().stream().filter((detail) -> detail.getPhoto() != null).forEach((detail) -> {
                String newFileUrl = fileUrlMap.get(detail.getPhoto());
                if (newFileUrl != null) {
                    detail.setPhoto(newFileUrl);
                }
            });
        }
        courseRepository.mergeCourse(newCourse);
    }

}
