package app.joycourse.www.prod.service;


import org.springframework.transaction.annotation.Transactional;

//@SpringBootTest(properties = {"nickname=ykh", "email=ykh8383633@naver.com", "gender=0", "ageRange=1"})
@Transactional
public class AccountServiceTest {
/*
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountService accountService;

    @Value("${nickname}")
    private String nickname;

    @Value("${email}")
    private String email;

    @Value("${gender}")
    private Integer gender;

    @Value("${ageRange}")
    private Integer ageRange;


    @Test
    public void 회원가입(){
        // given
        User newUser = new User();
        newUser.setNickname(nickname);
        newUser.setAgeRange(ageRange);
        newUser.setEmail(email);
        newUser.setGender(gender);
        newUser.setCreateAt();

        // when
        User user = accountService.saveUser(newUser);

        // then
        User findUser = accountRepository.findById(newUser.getId()).orElse(null);
        Assertions.assertThat(findUser.getAgeRange()).isEqualTo(user.getAgeRange());
        Assertions.assertThat(findUser.getGender()).isEqualTo(user.getGender());
        Assertions.assertThat(findUser.getNickname()).isEqualTo(user.getNickname());
        Assertions.assertThat(findUser.getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    public void 중복_회원_예외(){
        // given
        User user1 = new User();
        User user2 = new User();

        user1.setNickname(nickname);
        user1.setEmail(email);
        user1.setGender(gender);
        user1.setAgeRange(ageRange);
        User user = accountService.saveUser(user1);

        //when
        user2.setNickname(nickname);
        user2.setEmail("ykh8383633@gmail.com");
        CustomException e = assertThrows(CustomException.class, ()-> accountService.saveUser(user2));

        //then
        Assertions.assertThat(e.getMessage()).isEqualTo("User is already exist");
    }*/
    /*
    @Test
    public void 회원_삭제(){
        // given
        User newUser = new User();
        newUser.setNickname(nickname);
        newUser.setEmail(email);
        newUser.setGender(gender);
        newUser.setAgeRange(ageRange);
        User user = accountService.saveUser(newUser);

        User findUser = accountRepository.findById(user.getId()).orElse(null);
        Assertions.assertThat(findUser.getNickname()).isEqualTo(user.getNickname());
        System.out.println("////// save success //////////");

        // when
        accountService.deleteUser(findUser);
        User findDeleteUser = accountRepository.findById(findUser.getId()).orElse(null);

        //then
        System.out.println(findDeleteUser.getNickname());
        Assertions.assertThat(findDeleteUser).isNull();

    }*/

}
