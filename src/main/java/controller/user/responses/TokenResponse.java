package controller.user.responses;

public class TokenResponse {
    private String token;
    private String username;

    public TokenResponse(String token , String username) {
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public String getUsername(){
        return username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsername(String username){
        this.username = username;
    }
}
