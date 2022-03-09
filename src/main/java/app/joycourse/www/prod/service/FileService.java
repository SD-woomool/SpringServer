package app.joycourse.www.prod.service;

import app.joycourse.www.prod.dto.ImageFileDto;
import app.joycourse.www.prod.entity.ImageFile;
import app.joycourse.www.prod.entity.ImageFileType;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.repository.ImageFileRepository;
import app.joycourse.www.prod.util.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    @Value("${file-dir.path}")
    private String fileDir;
    @Value("${file-dir.download-url}")
    private String fileDownLoadUrl;

    private final ImageFileRepository imageFileRepository;

    @PostConstruct
    public void init() {
        try {
            for (ImageFileType imageFileType : ImageFileType.values()) {
                // 미리 디렉토리 생성 해두기
                Files.createDirectories(Paths.get(fileDir, imageFileType.getPath()));
            }
        } catch (IOException e) {
            log.error("[init] Fail to create FileService Bean.", e);
            throw new BeanCreationException("Fail to create FileService", e);
        }
    }

    public ImageFile uploadFile(ImageFileType imageFileType, @NotNull MultipartFile file) throws CustomException {
        // TODO: 파일 저장하는것도 비동기로하면 성능 향상 할 수 있을듯
        if (Objects.isNull(file) || file.isEmpty()) {
            throw new CustomException(CustomException.CustomError.MISSING_PARAMETERS);
        }

        String originalName = file.getOriginalFilename();
        if (Objects.isNull(originalName)) {
            throw new CustomException(CustomException.CustomError.INVALID_PARAMETER);
        }

        String fileFullName = StringUtils.cleanPath(originalName);
        String fileType = fileFullName.substring(fileFullName.lastIndexOf("."));

        String hashedFileName = HashUtil.sha256(imageFileType.getPath() + fileFullName + System.currentTimeMillis()) + fileType;
        String fileUrl = fileDownLoadUrl + "/" + hashedFileName;

        try {
            // 여기는 파일서버 역할, S3로 바뀌게되면 아래부분을 바꿔야함.
            Files.copy(file.getInputStream(), Paths.get(fileDir, imageFileType.getPath(), hashedFileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            log.error("[uploadFiles] Fail to upload.", e);
            throw new CustomException(CustomException.CustomError.SERVER_ERROR);
        }

        // DB에 파일 저장 - 향후 이미지 전처리도 고려하여 디비로 관리 ( 이 부분 확신이 없음.)
        ImageFile imageFile = new ImageFile();
        imageFile.setOriginalName(originalName);
        imageFile.setHashedName(hashedFileName);
        imageFile.setFileType(fileType);
        imageFile.setFileUrl(fileUrl);
        imageFile.setImageFileType(imageFileType);
        imageFile.setSizeByte(file.getSize());
        imageFileRepository.save(imageFile);

        return imageFile;
    }


    public void deleteFile(ImageFileType imageFileType, String fileName) {
        Optional<ImageFile> optionalImageFile = imageFileRepository.findByHashedName(fileName);
        if(optionalImageFile.isEmpty()) {
            log.error("[deleteFile] There is no file. {}", fileName);
            throw new CustomException(CustomException.CustomError.INVALID_PARAMETER);
        }

        // 여기서 부터 파일서버 역할, S3로 바뀌게되면 아래부분을 바꿔야함.
        Path targetFile = Paths.get(fileDir, imageFileType.getPath(), fileName);
        if (!targetFile.toFile().delete()) {
            log.error("[deleteFile] Fail to delete.");
        }
    }

    public ImageFileDto getFile(String fileName) {
        Optional<ImageFile> optionalImageFile = imageFileRepository.findByHashedName(fileName);
        if(optionalImageFile.isEmpty()) {
            log.error("[deleteFile] There is no file. {}", fileName);
            throw new CustomException(CustomException.CustomError.INVALID_PARAMETER);
        }
        ImageFileType imageFileType = optionalImageFile.get().getImageFileType();

        // 여기서 부터 파일서버 역할, S3로 바뀌게되면 아래부분을 바꿔야함.
        try {
            Path targetFile = Paths.get(fileDir, imageFileType.getPath(), fileName);
            return ImageFileDto.builder()
                    .contentType(Files.probeContentType(targetFile))
                    .data(FileUtils.readFileToByteArray(targetFile.toFile()))
                    .build();
        } catch (Exception e) {
            log.error("[getFile] Fail to get file. " + imageFileType.getPath() + "/" + fileName, e);
            throw new CustomException(CustomException.CustomError.SERVER_ERROR);
        }
    }
}
