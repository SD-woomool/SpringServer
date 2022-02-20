package app.joycourse.www.prod.entity.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@Setter
@RedisHash(value = "refreshToken", timeToLive = 10800)
public class RefreshToken {
    @Id
    private String token;
    @Indexed
    private String uid;
    @Indexed
    private String deviceId;
}
