package API.com.example.E_COMMERCY.service.tokenBlacklist;

public interface TokenBlacklistInterface {

    public void blacklistAccessToken(String token, long millis);
    public void blacklistRefreshToken(String token, long millis);
    public boolean isBlacklisted(String token);

}
