package app.joycourse.www.prod.entity;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Agreement {
    NONE("NONE"),
    BASIC("BASIC");

    private final String aggrement;
}
