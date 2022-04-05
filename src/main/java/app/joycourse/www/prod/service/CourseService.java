package app.joycourse.www.prod.service;

import app.joycourse.www.prod.dto.CourseInfoDto;
import app.joycourse.www.prod.dto.CourseListDto;
import app.joycourse.www.prod.dto.PhotoInfoDto;
import app.joycourse.www.prod.dto.PlaceInfoDto;
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

@Service
@Transactional
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseDetailRepository courseDetailRepository;
    private final PlaceRepository placeRepository;
    private final FileService fileService;


    public Course saveCourse(User user, CourseInfoDto courseInfo, List<MultipartFile> files) {
        Course course = new Course(courseInfo);
        course.setUser(user);
        courseInfo.getCourseDetail().stream().filter(Objects::nonNull).forEach((detailDto) -> {
            PlaceInfoDto placeDto = detailDto.getPlace();
            if (placeDto == null) {
                throw new CustomException(CustomException.CustomError.MISSING_PARAMETERS);
            }
            Place place = placeRepository.findById(placeDto.getId()).orElseThrow(() -> new CustomException(CustomException.CustomError.INVALID_PARAMETER));
            CourseDetail courseDetail = detailDto.convertToEntity();
            courseDetail.setCourse(course);
            courseDetail.setPlace(place);
            course.addCourseDetail(courseDetail);
            place.setCourseDetails(courseDetail);
        });
        course.setTotalPrice();
        if (files != null) {
            Map<String, String> fileUrlMap = fileService.uploadFiles(files, FileService.ImageFileType.COURSE_DETAIL_IMAGE);
            course.getCourseDetail().stream()
                    .filter((detail) -> detail.getPhoto() != null)
                    .forEach((detail) -> {
                        String newFileUrl = Optional.ofNullable(fileUrlMap.get(detail.getPhoto())).orElseThrow(() -> new CustomException(CustomException.CustomError.SERVER_ERROR));
                        detail.setPhoto(newFileUrl);
                    });
        }
        return courseRepository.saveCourse(course);
    }

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

    public void deleteCourse(User user, long courseId) {
        Course deleteCourse = courseRepository.findById(courseId).orElse(null);
        if (deleteCourse == null || deleteCourse.getUser() != user) {
            throw new CustomException(CustomException.CustomError.INVALID_PARAMETER);
        }
        courseRepository.deleteCourse(deleteCourse);
    }

    public Course getCourse(Long courseId) throws CustomException {
        return courseRepository.findById(courseId).orElseThrow(() ->
                new CustomException(CustomException.CustomError.INVALID_PARAMETER));
    }


    public void updateCourse(Course course, CourseInfoDto newCourseInfo, List<MultipartFile> files) {
        Course newCourse = new Course(newCourseInfo);
        newCourseInfo.getCourseDetail().stream().filter(Objects::nonNull).forEach((detailDto) -> {
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
            newCourse.getCourseDetail().stream().filter((detail) -> detail.getPhoto() != null).forEach((detail) -> {
                String newFileUrl = fileUrlMap.get(detail.getPhoto());
                if (newFileUrl != null) {
                    detail.setPhoto(newFileUrl);
                }
            });
        }
        courseRepository.mergeCourse(newCourse);
    }

}
