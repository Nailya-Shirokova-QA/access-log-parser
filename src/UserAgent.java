public class UserAgent {
    private String userAgentString;

    public UserAgent(String userAgentString) {
        this.userAgentString = userAgentString != null ? userAgentString : "";
    }

    public boolean isBot() {
        if (userAgentString == null || userAgentString.isEmpty()) {
            return false;
        }

        String lowerUA = userAgentString.toLowerCase();

        return lowerUA.contains("bot") ||
                lowerUA.contains("crawler") ||
                lowerUA.contains("spider") ||
                lowerUA.contains("googlebot") ||
                lowerUA.contains("bingbot") ||
                lowerUA.contains("yandexbot") ||
                lowerUA.contains("semrushbot") ||
                lowerUA.contains("ahrefsbot") ||
                lowerUA.contains("dotbot") ||
                lowerUA.contains("dataforseobot") ||
                lowerUA.contains("blexbot") ||
                lowerUA.contains("megaindex") ||
                lowerUA.contains("curl") ||
                lowerUA.contains("okhttp") ||
                lowerUA.contains("python") ||
                lowerUA.contains("java") ||
                lowerUA.contains("php") ||
                lowerUA.contains("ruby") ||
                lowerUA.contains("go-http");
    }

    public String getUserAgentString() {
        return userAgentString;
    }

    public void setUserAgentString(String userAgentString) {
        this.userAgentString = userAgentString;
    }

    @Override
    public String toString() {
        return userAgentString;
    }
}

