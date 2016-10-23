package soselab.easylearn.json.request;


public class AuthenticationRequest {

    private static final long serialVersionUID = 6624726180748515507L;
    private String id;
    private String token;

    public AuthenticationRequest() {
        super();
    }

    public AuthenticationRequest(String userId, String token) {
        super();
        this.id = userId;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "AuthenticationRequest{" +
                "id='" + id + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
