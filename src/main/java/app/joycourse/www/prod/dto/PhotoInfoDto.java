package app.joycourse.www.prod.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
//@Document(indexName = "photo")
public class PhotoInfoDto {
    private String fileUrl;
    private String fileName;
    private Boolean deleted;

    public PhotoInfoDto() {
    }
}
