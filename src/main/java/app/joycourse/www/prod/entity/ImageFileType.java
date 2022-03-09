package app.joycourse.www.prod.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ImageFileType {
    PROFILE("profile"),
    COURSE("course"),
    ;

    private final String path;
}
