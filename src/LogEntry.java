public class LogEntry {
    private String ipAddress;
    private String dateTime;
    private String request;
    private int responseCode;
    private int dataSize;
    private String referer;
    private String userAgent;

    public LogEntry(String ipAddress, String dateTime, String request,
                    int responseCode, int dataSize, String referer, String userAgent) {
        this.ipAddress = ipAddress;
        this.dateTime = dateTime;
        this.request = request;
        this.responseCode = responseCode;
        this.dataSize = dataSize;
        this.referer = referer;
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getRequest() {
        return request;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getDataSize() {
        return dataSize;
    }

    public String getReferer() {
        return referer;
    }

    public String getUserAgent() {
        return userAgent;
    }
}
