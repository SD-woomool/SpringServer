package app.joycourse.www.prod.controller;


import app.joycourse.www.prod.exception.CustomException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Api(tags = "코스")
@Controller
@RequestMapping("/files")
public class FileController {

    @Value("${file-dir.path}")
    private String fileDir;


    @ApiOperation(
            value = "파일을 가져온다.",
            notes = "코스 작성시 저장한 파일을 가져온다.\n",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.IMAGE_PNG_VALUE + ", " + MediaType.IMAGE_JPEG_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "성공"),
            @ApiResponse(code = 401, message = "인증 정보가 없는 경우"),
            @ApiResponse(code = 403, message = "권한이 없거나, 유효하지 않는 인증 정보인 경우")
    })
    @GetMapping("/{file-name}")
    public void downloadFile(
            HttpServletResponse response,
            @PathVariable("file-name") String fileName
    ) throws IOException {
        Path targetFile = Paths.get(fileDir, "course", fileName);
        if (!Files.exists(targetFile)) {
            throw new CustomException(CustomException.CustomError.INVALID_PARAMETER);
        }
        String fileType = Files.probeContentType(targetFile);
        response.setContentType(fileType);
        response.getOutputStream().write(FileUtils.readFileToByteArray(targetFile.toFile()));
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

}
