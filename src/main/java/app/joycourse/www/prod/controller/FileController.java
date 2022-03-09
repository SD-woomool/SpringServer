package app.joycourse.www.prod.controller;


import app.joycourse.www.prod.dto.ImageFileDto;
import app.joycourse.www.prod.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("files")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @GetMapping("/{fileName}")
    public void downloadFile(HttpServletResponse response, @PathVariable String fileName) throws IOException {
        ImageFileDto imageFileDto = fileService.getFile(fileName);

        response.setContentType(imageFileDto.getContentType());
        response.getOutputStream().write(imageFileDto.getData());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }
}
