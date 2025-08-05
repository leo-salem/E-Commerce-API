package API.com.example.E_COMMERCY.service.token;

public interface TokenInterface {
    public void saveRefreshToken(String username, String token, long millis);
    public void saveAccessToken(String username, String token, long millis);
    public String getRefreshToken(String username);
    public String getAccessToken(String username);
    public void deleteRefreshToken(String username);
    public void deleteAccessToken(String username);
    public boolean isRefreshTokenValid(String username, String Token);
    public boolean isAccessTokenValid(String username, String Token);
}
