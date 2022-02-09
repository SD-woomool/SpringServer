package app.joycourse.www.prod.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Gender {
    PRIVATE("PRIVATE"),
    FEMALE("FEMALE"),
    MALE("MALE");

    private final String gender;
}
