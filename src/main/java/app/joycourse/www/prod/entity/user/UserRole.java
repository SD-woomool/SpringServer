package app.joycourse.www.prod.entity.user;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserRole {
    NORMAL("NORMAL"),
    BLOCK("BLOCK"),
    ADMIN("ADMIN");

    private final String role;
}
