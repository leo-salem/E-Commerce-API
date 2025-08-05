package API.com.example.E_COMMERCY.service.tokenBlacklist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class TokenBlacklistService implements TokenBlacklistInterface{

    private final RedisTemplate<String, String> redisTemplate;
    private static final String PREFIX_REFRESH_BLACKLIST = "blacklist:refresh:";
    private static final String PREFIX_ACCESS_BLACKLIST = "blacklist:access:";

    @Autowired
    public TokenBlacklistService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void blacklistAccessToken(String token, long millis) {
        redisTemplate.opsForValue().set(PREFIX_ACCESS_BLACKLIST + token, "true", Duration.ofMillis(millis));
    }

    @Override
    public void blacklistRefreshToken(String token, long millis) {
        redisTemplate.opsForValue().set(PREFIX_REFRESH_BLACKLIST + token, "true", Duration.ofMillis(millis));
    }

    @Override
    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey(PREFIX_ACCESS_BLACKLIST + token) || redisTemplate.hasKey(PREFIX_REFRESH_BLACKLIST + token);
    }
}
