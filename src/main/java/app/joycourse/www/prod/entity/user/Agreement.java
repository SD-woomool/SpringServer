package app.joycourse.www.prod.entity.user;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Agreement {
    NONE("NONE"),
    BASIC("BASIC");

    private final String aggrement;
}
