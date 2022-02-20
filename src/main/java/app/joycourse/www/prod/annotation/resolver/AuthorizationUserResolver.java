package app.joycourse.www.prod.annotation.resolver;

import app.joycourse.www.prod.annotation.AuthorizationUser;
import app.joycourse.www.prod.entity.auth.Auth;
import app.joycourse.www.prod.entity.user.User;
import app.joycourse.www.prod.exception.CustomException;
import app.joycourse.www.prod.repository.AuthRepository;
import app.joycourse.www.prod.repository.UserRepository;
import app.joycourse.www.prod.service.auth.ManageAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorizationUserResolver implements HandlerMethodArgumentResolver {
    private final UserRepository userRepository;
    private final ManageAuthService manageAuthService;
    private final AuthRepository authRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthorizationUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        AuthorizationUser authorizationUser = parameter.getParameterAnnotation(AuthorizationUser.class);
        assert authorizationUser != null;

        String uid = manageAuthService.parseAccessToken((HttpServletRequest) webRequest.getNativeRequest());
        Optional<Auth> optionalAuth = authRepository.findByUid(uid);
        if (optionalAuth.isEmpty()) {
            if (authorizationUser.whenEmptyThrow()) {
                throw new CustomException(CustomException.CustomError.UNAUTHORIZED);
            } else {
                return null;
            }
        }

        Optional<User> optionalUser = userRepository.findByUid(uid);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            if (authorizationUser.whenEmptyThrow()) {
                throw new CustomException(CustomException.CustomError.UNAUTHORIZED);
            } else {
                return null;
            }
        }
    }
}
