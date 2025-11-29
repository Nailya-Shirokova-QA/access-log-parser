import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
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

    public int getPeakVisitsPerSecond() {
        Map<Long, Long> visitsPerSecond = logEntries.stream()
                .filter(entry -> !entry.isBot())
                .collect(Collectors.groupingBy(
                        LogEntry::getTimestampInSeconds,
                        Collectors.counting()
                ));

        if (visitsPerSecond.isEmpty()) {
            return 0;
        }

        Optional<Long> maxVisits = visitsPerSecond.values().stream()
                .max(Long::compareTo);

        return maxVisits.map(Long::intValue).orElse(0);
    }

    public Set<String> getReferringSites() {
        return logEntries.stream()
                .map(LogEntry::getReferer)
                .filter(referer -> referer != null && !referer.isEmpty())
                .map(this::extractDomainFromUrl)
                .filter(domain -> domain != null && !domain.isEmpty())
                .collect(Collectors.toSet());
    }

    private String extractDomainFromUrl(String url) {
        try {
            if (url == null || url.isEmpty() || url.equals("-")) {
                return "";
            }

            if (url.
                    startsWith("http://")) {
                url = url.substring(7);
            } else if (url.startsWith("https://")) {
                url = url.substring(8);
            }

            int slashIndex = url.indexOf('/');
            if (slashIndex > 0) {
                url = url.substring(0, slashIndex);
            }

            int colonIndex = url.indexOf(':');
            if (colonIndex > 0) {
                url = url.substring(0, colonIndex);
            }

            if (url.contains(" ")) {
                return "";
            }

            return url;
        } catch (Exception e) {
            return "";
        }
    }

    public int getMaxVisitsBySingleUser() {
        Map<String, Long> visitsPerUser = logEntries.stream()
                .filter(entry -> !entry.isBot())
                .collect(Collectors.groupingBy(
                        LogEntry::getIpAddress,
                        Collectors.counting()
                ));

        if (visitsPerUser.isEmpty()) {
            return 0;
        }

        Optional<Long> maxVisits = visitsPerUser.values().stream()
                .max(Long::compareTo);

        return maxVisits.map(Long::intValue).orElse(0);
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

    public void printTimeRange() {
        if (logEntries.isEmpty()) {
            System.out.println("No log entries");
            return;
        }

        LocalDateTime earliest = logEntries.stream()
                .map(LogEntry::getTimestamp)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        LocalDateTime latest = logEntries.stream()
                .map(LogEntry::getTimestamp)
                .max(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now());

        System.out.println("Time range: " + earliest + " to " + latest);
        System.out.println("Duration in hours: " + Duration.between(earliest, latest).toHours());
    }
}
