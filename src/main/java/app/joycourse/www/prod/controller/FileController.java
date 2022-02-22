package app.joycourse.www.prod.controller;


import app.joycourse.www.prod.exception.CustomException;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Paths;

@Controller
@RequestMapping("files")
public class FileController {

    private final String fileDir = ".\\images";


    @GetMapping("/{file-name}")
    public void downloadFile(
            HttpServletResponse response,
            @PathVariable("file-name") String fileName
    ) throws IOException {
        File targetFile = Paths.get(fileDir, "course", fileName).toFile();
        if (!targetFile.isFile()) {
            throw new CustomException(CustomException.CustomError.INVALID_PARAMETER);
        }
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(targetFile.getName(), "UTF-8") + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.getOutputStream().write(FileUtils.readFileToByteArray(targetFile));
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

}
