import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Statistics {
    private List<LogEntry> logEntries;
    private int errorCount;
    private int nonBotRequestsCount;

    public Statistics() {
        this.logEntries = new ArrayList<>();
        this.errorCount = 0;
        this.nonBotRequestsCount = 0;
    }

    public void addEntry(LogEntry entry) {
        logEntries.add(entry);

        if (entry.isErrorResponse()) {
            errorCount++;
        }

        if (!entry.isBot()) {
            nonBotRequestsCount++;
        }
    }

    public double getAverageVisitsPerHour() {
        if (logEntries.isEmpty() || nonBotRequestsCount == 0) {
            return 0.0;
        }

        List<LogEntry> nonBotEntries = logEntries.stream()
                .filter(entry -> !entry.isBot())
                .collect(Collectors.toList());

        if (nonBotEntries.isEmpty()) {
            return 0.0;
        }

        LocalDateTime earliest = nonBotEntries.stream()
                .map(LogEntry::getTimestamp)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        LocalDateTime latest = nonBotEntries.stream()
                .map(LogEntry::getTimestamp)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        long hours = Duration.between(earliest, latest).toHours();
        if (hours == 0) {
            hours = 1;
        }

        return (double) nonBotRequestsCount / hours;
    }

    public double getAverageErrorsPerHour() {
        if (logEntries.isEmpty() || errorCount == 0) {
            return 0.0;
        }

        LocalDateTime earliest = logEntries.stream()
                .map(LogEntry::getTimestamp)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        LocalDateTime latest = logEntries.stream()
                .map(LogEntry::getTimestamp)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        long hours = Duration.between(earliest, latest).toHours();
        if (hours == 0) {
            hours = 1;
        }

        return (double) hours / errorCount;
    }

    public double getAverageVisitsPerUser() {
        if (nonBotRequestsCount == 0) {
            return 0.0;
        }

        Set<String> uniqueNonBotIps = logEntries.stream()
                .filter(entry -> !entry.isBot())
                .map(LogEntry::getIpAddress)
                .collect(Collectors.toSet());

        if (uniqueNonBotIps.isEmpty()) {
            return 0.0;
        }

        return (double) nonBotRequestsCount / uniqueNonBotIps.size();
    }

    public int getErrorCount() {
        return errorCount;
    }

    public int getNonBotRequestsCount() {
        return nonBotRequestsCount;
    }

    public int getTotalEntries() {
        return logEntries.size();
    }

    public List<LogEntry> getLogEntries() {
        return new ArrayList<>(logEntries);
    }
}
