package API.com.example.E_COMMERCY.service.token;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class TokenService implements TokenInterface {

    private static final String PREFIX_REFRESH = "refresh:";
    private static final String PREFIX_ACCESS = "access:";
    private final RedisTemplate<String, String> redisTemplate;
    public TokenService(RedisTemplate<String, String> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveRefreshToken(String username, String token, long millis) {
        redisTemplate.opsForValue().set(PREFIX_REFRESH + username, token, Duration.ofMillis(millis));
    }

    @Override
    public void saveAccessToken(String username, String token, long millis) {
        redisTemplate.opsForValue().set(PREFIX_ACCESS + username, token, Duration.ofMillis(millis));
    }

    @Override
    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get(PREFIX_REFRESH + username);
    }

    @Override
    public String getAccessToken(String username) {
        return redisTemplate.opsForValue().get(PREFIX_ACCESS + username);
    }

    @Override
    public void deleteRefreshToken(String username) {
        redisTemplate.delete(PREFIX_REFRESH + username);
    }

    @Override
    public void deleteAccessToken(String username) {
        redisTemplate.delete(PREFIX_ACCESS + username);
    }

    @Override
    public boolean isRefreshTokenValid(String username, String Token) {
        String stored = getRefreshToken(username);
        return stored != null && stored.equals( Token);
    }

    @Override
    public boolean isAccessTokenValid(String username, String Token) {
        String stored = getAccessToken(username);
        return stored != null && stored.equals(Token);
    }
}
