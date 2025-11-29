public class UserAgent {
    private String userAgentString;

    public UserAgent(String userAgentString) {
        this.userAgentString = userAgentString != null ? userAgentString : "";
    }

    public boolean isBot() {
        return userAgentString.toLowerCase().contains("bot");
    }

    public String getUserAgentString() {
        return userAgentString;
    }

    public void setUserAgentString(String userAgentString) {
        this.userAgentString = userAgentString;
    }
}
