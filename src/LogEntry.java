import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogEntry {
    private final String ipAddress;
    private final LocalDateTime dateTime;
    private final HttpMethod httpMethod;
    private final String path;
    private final int responseCode;
    private final int dataSize;
    private final String referer;
    private final UserAgent userAgent;

    private static final Pattern LOG_PATTERN = Pattern.compile(
            "^([\\d.]+)\\s+-\\s+-\\s+\\[([^\\]]+)\\]\\s+\"(\\w+)\\s+([^\\s?]+)(?:\\?[^\"]*)?\\s+HTTP/[\\d.]+\"\\s+(\\d{3})\\s+(-|\\d+)\\s+\"([^\"]*)\"\\s+\"([^\"]*)\"$"
    );

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", java.util.Locale.US);

    public LogEntry(String logLine) {
        Matcher matcher = LOG_PATTERN.matcher(logLine);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid log format: " +
                    (logLine.length() > 100 ? logLine.substring(0, 100) + "..." : logLine));
        }

        this.ipAddress = matcher.group(1);

        String dateStr = matcher.group(2);
        this.dateTime = parseDateTime(dateStr);

        this.httpMethod = HttpMethod.fromString(matcher.group(3));
        this.path = matcher.group(4);
        this.responseCode = Integer.parseInt(matcher.group(5));

        String sizeStr = matcher.group(6);
        this.dataSize = "-".equals(sizeStr) ? 0 : Integer.parseInt(sizeStr);

        String ref = matcher.group(7);
        this.referer = "-".equals(ref) || ref.isEmpty() ? "none" : ref;

        String ua = matcher.group(8);
        this.userAgent = new UserAgent(ua);
    }

    private LocalDateTime parseDateTime(String dateStr) {
        try {
            return LocalDateTime.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Cannot parse date: " + dateStr, e);
        }
    }

    public String getIpAddress() { return ipAddress; }
    public LocalDateTime getDateTime() { return dateTime; }
    public HttpMethod getHttpMethod() { return httpMethod; }
    public String getPath() { return path; }
    public int getResponseCode() { return responseCode; }
    public int getDataSize() { return dataSize; }
    public String getReferer() { return referer; }
    public UserAgent getUserAgent() { return userAgent; }

    @Override
    public String toString() {
        return String.format("LogEntry{ip='%s', time=%s, method=%s, path='%s', code=%d, size=%d}",
                ipAddress, dateTime, httpMethod, path, responseCode, dataSize);
    }
}
