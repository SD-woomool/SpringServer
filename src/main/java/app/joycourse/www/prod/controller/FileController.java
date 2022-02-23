package app.joycourse.www.prod.controller;


import app.joycourse.www.prod.exception.CustomException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("files")
public class FileController {

    @Value("${file-dir.path}")
    private String fileDir;


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
        response.setContentType("application/" + fileType);
        response.getOutputStream().write(FileUtils.readFileToByteArray(targetFile.toFile()));
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

}
