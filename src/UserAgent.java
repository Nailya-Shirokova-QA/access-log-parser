public class UserAgent {
    private final Browser browser;
    private final OperatingSystem operatingSystem;

    public UserAgent(String userAgentString) {
        this.browser = Browser.detect(userAgentString);
        this.operatingSystem = OperatingSystem.detect(userAgentString);
    }

    public Browser getBrowser() {
        return browser;
    }

    public OperatingSystem getOperatingSystem() {
        return operatingSystem;
    }
}
