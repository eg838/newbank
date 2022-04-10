package newbank.server;

public class UserSession {
    private int attempts;
    private String userName;

    public UserSession(String userName){
        this.attempts=0;
        this.userName=userName;
    }

    public void incrementAttempt(){
        this.attempts++;
    }

    public int getAttempts(){
        return this.attempts;
    }

    public String getUsername(){
        return this.userName;
    }
}
