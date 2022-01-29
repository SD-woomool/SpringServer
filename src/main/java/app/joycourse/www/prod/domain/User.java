package app.joycourse.www.prod.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.SimpleDateFormat;


import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.List;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 이것도 찾아보자. 디비를 뒤져서 제일 높은수를 주는건지?
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(unique = true, nullable = false)
    private String email;

    @Column
    private Integer ageRange;

    @Column
    private Integer gender;

    @Column
    private String profileImageUrl;

    @Column
    private String createdAt;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "user")
    private List<Course> course; // manytomany 나 onetomany는 이렇게 해야함?


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(Integer ageRange) {
        this.ageRange = ageRange;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreateAt() {
        long millis = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.createdAt = format.format(millis);
    }

}

