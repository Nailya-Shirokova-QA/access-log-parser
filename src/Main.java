import java.util.List;

public class Main {
    public static void main(String[] args) {
        String logFilePath = "access.log";

        List<LogEntry> entries = LogFileParser.parseLogFile(logFilePath);

        Statistics stats = new Statistics();
        for (LogEntry entry : entries) {
            stats.addEntry(entry);
        }

        System.out.println("=== Web Server Statistics ===");
        System.out.println("Total log entries: " + stats.getTotalEntries());
        System.out.println("Non-bot requests: " + stats.getNonBotRequestsCount());
        System.out.println("Error responses: " + stats.getErrorCount());
        System.out.println();
        System.out.println("Average visits per hour: " + String.format("%.2f", stats.getAverageVisitsPerHour()));
        System.out.println("Average errors per hour: " + String.format("%.2f", stats.getAverageErrorsPerHour()));
        System.out.println("Average visits per user: " + String.format("%.2f", stats.getAverageVisitsPerUser()));
    }
}
