package app.joycourse.www.prod.entity.user;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Gender {
    PRIVATE("PRIVATE"),
    FEMALE("FEMALE"),
    MALE("MALE");

    private final String gender;
}
