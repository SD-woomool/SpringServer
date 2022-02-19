package app.joycourse.www.prod.entity.user;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AgeRange {
    PRIVATE("PRIVATE"),
    TEENS("TEENS"),
    TWENTIES("TWENTIES"),
    THIRTIES("THIRTIES"),
    REST("REST");

    private final String key;
}