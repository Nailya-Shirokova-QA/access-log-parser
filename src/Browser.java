public enum Browser {
    EDGE("Edge"),
    FIREFOX("Firefox"),
    CHROME("Chrome"),
    OPERA("Opera"),
    SAFARI("Safari"),
    OTHER("Other");

    private final String displayName;

    Browser(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static Browser detect(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) return OTHER;

        String ua = userAgent.toLowerCase();
        if (ua.contains("edge")) return EDGE;
        if (ua.contains("firefox")) return FIREFOX;
        if (ua.contains("chrome")) return CHROME;
        if (ua.contains("opera")) return OPERA;
        if (ua.contains("safari")) return SAFARI;  // И ЭТУ

        return OTHER;
    }
}
