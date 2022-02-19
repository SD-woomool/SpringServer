package app.joycourse.www.prod.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class UserController {
//    private final UserService service;
//    private final JwtService jwtService;
//    private final UserRepository userRepository;
//    private final JwtConfig jwtConfig;
//
//    @GetMapping("/nickname")
//    @ResponseBody
//    public Response<Map<String, Boolean>> checkNickname(@RequestParam(value = "nickname", required = false) String nickname) { // 닉네임 길이 체크 등등 추가할게 있나?
//        Optional<User> user = userRepository.findByNickname(nickname);
//        Map<String, Boolean> data = new HashMap<>();
//        if (user.isPresent() || nickname == null || nickname.length() < 3) {
//            data.put("login", false);
//            data.put("check", false);
//        } else {
//            data.put("login", false);
//            data.put("check", true);
//        }
//        return new Response<>(data);
//    }
//
//    @PutMapping("/")
//    @ResponseBody
//    public Response<Map<String, Boolean>> edit(@RequestBody User userInfo, HttpServletRequest request) {
//        try {
//            Optional<User> optionalUser = Optional.ofNullable((User) request.getAttribute("user"));
//            User user = optionalUser.orElseThrow(() -> new CustomException("NO_USER", CustomException.CustomError.MISSING_PARAMETERS));
//
//            service.updateUser(user, userInfo);
//        } catch (ClassCastException e) {
//            throw new CustomException("UPDATE_ERROR", CustomException.CustomError.SERVER_ERROR);
//        }
//        Map<String, Boolean> data = new HashMap<>();
//        data.put("edit", true);
//        return new Response<>(data);
//    }
}
