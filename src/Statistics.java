import java.time.Duration;
import java.time.LocalDateTime;

public class Statistics {
    private long totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = null;
        this.maxTime = null;
    }

    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getDataSize();

        LocalDateTime entryTime = entry.getDateTime();

        if (minTime == null) {
            minTime = entryTime;
            maxTime = entryTime;
        } else {
            if (entryTime.isBefore(minTime)) {
                minTime = entryTime;
            }
            if (entryTime.isAfter(maxTime)) {
                maxTime = entryTime;
            }
        }
    }

    public double getTrafficRate() {
        if (minTime == null || maxTime == null || minTime.equals(maxTime)) {
            return 0.0;
        }

        long hours = Duration.between(minTime, maxTime).toHours();
        if (hours == 0) {
            hours = 1;
        }

        return (double) totalTraffic / hours;
    }

    public long getTotalTraffic() {
        return totalTraffic;
    }

    public LocalDateTime getMinTime() {
        return minTime;
    }

    public LocalDateTime getMaxTime() {
        return maxTime;
    }
}
