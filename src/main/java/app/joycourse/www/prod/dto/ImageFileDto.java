package app.joycourse.www.prod.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ImageFileDto {
    private String contentType;
    private byte[] data;
}
