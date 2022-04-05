package app.joycourse.www.prod.annotation.resolver;

import app.joycourse.www.prod.annotation.AuthorizationUser;
import app.joycourse.www.prod.entity.auth.Auth;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.service.UserService;
import app.joycourse.www.prod.service.auth.AuthService;
import app.joycourse.www.prod.service.auth.ManageAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorizationUserResolver implements HandlerMethodArgumentResolver {
    private final ManageAuthService manageAuthService;
    private final AuthService authService;
    private final UserService userService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthorizationUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        AuthorizationUser authorizationUser = parameter.getParameterAnnotation(AuthorizationUser.class);
        assert authorizationUser != null;

        // TODO: real 환경에서 제거 해야함!!************* only dev!!
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        if (ObjectUtils.nullSafeEquals(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION), "tony")) {
            return userService.getUserByNickname("tony").orElse(null);
        } else if (ObjectUtils.nullSafeEquals(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION), "ironman")) {
            return userService.getUserByNickname("ironman").orElse(null);
        }

        String uid = manageAuthService.parseAccessToken((HttpServletRequest) webRequest.getNativeRequest());
        Optional<Auth> optionalAuth = authService.getAuthByUid(uid);
        if (optionalAuth.isEmpty()) {
            if (authorizationUser.whenEmptyThrow()) {
                throw new CustomException(CustomException.CustomError.UNAUTHORIZED);
            } else {
                return null;
            }
        }
        Optional<User> optionalUser = userService.getUserByUid(uid);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (!user.getIsSigned()) {
                throw new CustomException(CustomException.CustomError.UNSIGNED);
            }
            return user;
        } else {
            if (authorizationUser.whenEmptyThrow()) {
                throw new CustomException(CustomException.CustomError.UNAUTHORIZED);
            } else {
                return null;
            }
        }
    }
}
