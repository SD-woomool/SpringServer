package app.joycourse.www.prod.service;

import app.joycourse.www.prod.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class FileService {
    //@Value("${file-dir.path}")
    private final String fileDir = ".\\images";

    /*
     * 파일 이름 고치는거(파일이름에 뭘 더 추가할지 생각해봐
     * 그리고 컨트롤러 가서 나머지 save 로직하면 됨
     */
    public List<String> fileUpload(List<MultipartFile> files, ImageFileType imageFileType) {
        List<String> fileNameList = new ArrayList<>();
        files.stream().filter(Objects::nonNull).forEach((file) -> {
            String fileName = Optional.ofNullable(file.getOriginalFilename()).orElseThrow(() -> new CustomException(CustomException.CustomError.MISSING_PARAMETERS));
            int splitPoint = fileName.lastIndexOf(".");
            String newFileName = StringUtils.cleanPath(fileName.substring(0, splitPoint))
                    + "_" + String.valueOf(System.currentTimeMillis()) + "." + fileName.substring(splitPoint + 1);

            Path dirLocation = Paths.get(fileDir
                    , imageFileType.getPath()
                    , newFileName);
            try {
                if (!Files.isDirectory(dirLocation)) {
                    dirLocation = Files.createDirectories(dirLocation);
                }
                Files.copy(Objects.requireNonNull(file).getInputStream(), dirLocation, StandardCopyOption.REPLACE_EXISTING);
                fileNameList.add(newFileName);
            } catch (IOException e) {
                e.printStackTrace();
                throw new CustomException(CustomException.CustomError.INVALID_PARAMETER);
            }
        });
        return fileNameList;
    }

    @AllArgsConstructor
    @Getter
    public enum ImageFileType {
        PROFILE_IMAGE("profile"),
        COURSE_DETAIL_IMAGE("course");

        private String path;
    }

}
