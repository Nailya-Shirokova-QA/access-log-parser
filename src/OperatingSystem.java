public enum OperatingSystem {
    WINDOWS("Windows"),
    MACOS("macOS"),
    LINUX("Linux"),
    ANDROID("Android"),
    IOS("iOS"),
    OTHER("Other");

    private final String displayName;

    OperatingSystem(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static OperatingSystem detect(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) return OTHER;

        String ua = userAgent.toLowerCase();
        if (ua.contains("windows")) return WINDOWS;
        if (ua.contains("mac os") || ua.contains("macintosh") || ua.contains("macos")) return MACOS;
        if (ua.contains("linux") && !ua.contains("android")) return LINUX;
        if (ua.contains("android")) return ANDROID;
        if (ua.contains("iphone") || ua.contains("ipad") || ua.contains("ios")) return IOS;

        return OTHER;
    }
}
