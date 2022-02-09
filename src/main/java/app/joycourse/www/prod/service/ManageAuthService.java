package app.joycourse.www.prod.service;

import app.joycourse.www.prod.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

public interface ManageAuthService {
    void saveAuth(HttpServletRequest request, HttpServletResponse response, User user);

    Optional<User> loadAuth(HttpServletRequest request, HttpServletResponse response);
}
