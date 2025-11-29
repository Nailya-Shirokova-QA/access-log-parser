import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        String logFilePath = "access.log";

        List<LogEntry> entries = LogFileParser.parseLogFile(logFilePath);

        System.out.println("Parsed entries: " + entries.size());

        if (!entries.isEmpty()) {
            System.out.println("First entry: " + entries.get(0));
            System.out.println("First entry timestamp: " + entries.get(0).getTimestamp());
            System.out.println("First entry user agent: " + entries.get(0).getUserAgent().getUserAgentString());
            System.out.println("First entry is bot: " + entries.get(0).isBot());
        }

        Statistics stats = new Statistics();
        for (LogEntry entry : entries) {
            stats.addEntry(entry);
        }

        stats.printTimeRange();

        System.out.println("\n=== Web Server Statistics ===");
        System.out.println("Total log entries: " + stats.getTotalEntries());
        System.out.println("Non-bot requests: " + stats.getNonBotRequestsCount());
        System.out.println("Error responses: " + stats.getErrorCount());
        System.out.println();
        System.out.println("Average visits per hour: " + String.format("%.2f", stats.getAverageVisitsPerHour()));
        System.out.println("Average errors per hour: " + String.format("%.2f", stats.getAverageErrorsPerHour()));
        System.out.println("Average visits per user: " + String.format("%.2f", stats.getAverageVisitsPerUser()));
        System.out.println();
        System.out.println("Peak visits per second: " + stats.getPeakVisitsPerSecond());
        System.out.println("Max visits by single user: " + stats.getMaxVisitsBySingleUser());

        Set<String> referringSites = stats.getReferringSites();
        System.out.println("Number of referring sites: " + referringSites.size());
        if (!referringSites.isEmpty()) {
            System.out.println("First 5 referring sites:");
            referringSites.stream().limit(5).forEach(System.out::println);
        }
    }
}
