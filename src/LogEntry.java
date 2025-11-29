import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LogEntry {
    private String ipAddress;
    private LocalDateTime timestamp;
    private UserAgent userAgent;
    private int responseCode;
    private String request;

    public LogEntry(String logLine) {
        parseLogLine(logLine);
    }

    private void parseLogLine(String logLine) {
        try {
            if (logLine == null || logLine.trim().isEmpty()) {
                return;
            }

            String[] parts = logLine.split("\\s+");

            if (parts.length >= 3) {
                this.ipAddress = parts[0];

                String dateTimeStr = parts[2];
                dateTimeStr = dateTimeStr.replace("[", "").replace("]", "");

                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm");
                    this.timestamp = LocalDateTime.parse(dateTimeStr, formatter);
                } catch (DateTimeParseException e) {
                    try {
                        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss");
                        this.timestamp = LocalDateTime.parse(dateTimeStr, formatter2);
                    } catch (DateTimeParseException e2) {
                        this.timestamp = LocalDateTime.now();
                    }
                }

                String userAgentString = "";
                if (parts.length > 3) {
                    StringBuilder uaBuilder = new StringBuilder();
                    for (int i = 3; i < parts.length; i++) {
                        uaBuilder.append(parts[i]).append(" ");
                    }
                    userAgentString = uaBuilder.toString().replace("\"", "");
                }

                this.userAgent = new UserAgent(userAgentString);
                this.responseCode = 200;
                this.request = "";
            }
        } catch (Exception e) {
            System.err.println("Error parsing log line: " + logLine);
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

    @Override
    public String toString() {
        return "LogEntry{ipAddress='" + ipAddress + "', timestamp=" + timestamp + "}";
    }
}

