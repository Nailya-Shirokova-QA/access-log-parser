import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class LogEntry {
    private String ipAddress;
    private LocalDateTime timestamp;
    private UserAgent userAgent;
    private int responseCode;
    private String request;
    private String referer;

    private static final DateTimeFormatter LOG_DATE_FORMATTER = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("dd/MMM/yyyy:HH:mm:ss")
            .toFormatter(Locale.ENGLISH);

    public LogEntry(String logLine) {
        parseLogLine(logLine);
    }

    private void parseLogLine(String logLine) {
        this.ipAddress = "";
        this.timestamp = LocalDateTime.now();
        this.userAgent = new UserAgent("");
        this.responseCode = 200;
        this.request = "";
        this.referer = "";

        if (logLine == null || logLine.trim().isEmpty()) {
            return;
        }

        String line = logLine.trim();

        try {
            int bracketStart = line.indexOf('[');
            int bracketEnd = line.indexOf(']');

            if (bracketStart > 0 && bracketEnd > bracketStart) {
                String ipPart = line.substring(0, bracketStart).trim();
                String[] ipParts = ipPart.split("\\s+");
                if (ipParts.length > 0) {
                    this.ipAddress = ipParts[0];
                }

                String timeStr = line.substring(bracketStart + 1, bracketEnd);
                this.timestamp = parseTimeFromString(timeStr);

                String afterBracket = line.substring(bracketEnd + 1).trim();

                String[] quoteParts = afterBracket.split("\"");

                if (quoteParts.length >= 2) {
                    this.request = quoteParts[1].trim();
                }

                String lastPart = "";
                if (quoteParts.length > 0) {
                    lastPart = quoteParts[quoteParts.length - 1].trim();
                }

                String[] parts = afterBracket.split("\\s+");
                for (String part : parts) {
                    if (part.matches("\\d{3}")) {
                        try {
                            this.responseCode = Integer.
                                    parseInt(part);
                            break;
                        } catch (NumberFormatException e) {
                            this.responseCode = 200;
                        }
                    }
                }

                String refererValue = "-";
                String userAgentValue = "";

                if (quoteParts.length >= 6) {
                    refererValue = quoteParts[3].trim();
                    userAgentValue = quoteParts[5].trim();
                } else if (quoteParts.length >= 4) {
                    userAgentValue = quoteParts[3].trim();
                }

                if (!refererValue.equals("-")) {
                    this.referer = refererValue;
                }

                this.userAgent = new UserAgent(userAgentValue);
            }
        } catch (Exception e) {
        }
    }

    private LocalDateTime parseTimeFromString(String timeStr) {
        try {
            String dateTimePart = timeStr.split("\\s+")[0];
            return LocalDateTime.parse(dateTimePart, LOG_DATE_FORMATTER);
        } catch (Exception e) {
            try {
                DateTimeFormatter altFormatter = new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("dd/MMM/yyyy:HH:mm")
                        .toFormatter(Locale.ENGLISH);
                String dateTimePart = timeStr.split("\\s+")[0];
                return LocalDateTime.parse(dateTimePart, altFormatter);
            } catch (Exception e2) {
                return LocalDateTime.now();
            }
        }
    }

    public boolean isBot() {
        return userAgent != null && userAgent.isBot();
    }

    public boolean isErrorResponse() {
        return responseCode >= 400 && responseCode < 600;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public UserAgent getUserAgent() {
        return userAgent;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getRequest() {
        return request;
    }

    public String getReferer() {
        return referer;
    }

    public long getTimestampInSeconds() {
        return timestamp.toEpochSecond(java.time.ZoneOffset.UTC);
    }

    @Override
    public String toString() {
        return String.format("IP: %s, Time: %s, Code: %d, Bot: %b, UA: %s",
                ipAddress, timestamp, responseCode, isBot(),
                userAgent != null ? userAgent.getUserAgentString() : "");
    }
}
