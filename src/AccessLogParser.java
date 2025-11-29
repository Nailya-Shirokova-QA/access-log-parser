import java.util.List;
import java.util.Map;

public class AccessLogParser {
    private Statistics statistics;

    public AccessLogParser() {
        this.
                statistics = new Statistics();
    }

    public void parseLogEntry(String logLine) {
        LogEntry entry = parseLine(logLine);
        if (entry != null) {
            statistics.addEntry(entry);
        }
    }

    private LogEntry parseLine(String logLine) {
        try {
            String[] parts = logLine.split(" ");

            if (parts.length < 12) {
                return null;
            }

            String ipAddress = parts[0];

            String dateTime = "";
            if (parts.length > 4) {
                dateTime = parts[3] + " " + parts[4];
            }

            String request = "";
            if (parts.length > 7) {
                request = parts[5] + " " + parts[6] + " " + parts[7];
            }

            int responseCode = 0;
            try {
                responseCode = Integer.parseInt(parts[8]);
            } catch (NumberFormatException e) {
                return null;
            }

            int dataSize = 0;
            try {
                dataSize = Integer.parseInt(parts[9]);
            } catch (NumberFormatException e) {
            }

            String referer = "";
            if (parts.length > 10) {
                referer = parts[10].replace("\"", "");
            }

            StringBuilder userAgentBuilder = new StringBuilder();
            for (int i = 11; i < parts.length; i++) {
                userAgentBuilder.append(parts[i]).append(" ");
            }
            String userAgent = userAgentBuilder.toString().trim();

            return new LogEntry(ipAddress, dateTime, request, responseCode, dataSize, referer, userAgent);

        } catch (Exception e) {
            return null;
        }
    }

    public List<String> getNotFoundPages() {
        return statistics.getAllNotFoundPages();
    }

    public Map<String, Double> getBrowserStatistics() {
        return statistics.getBrowserStatistics();
    }

    public void printStatistics() {
        System.out.println("Not found pages (404): " + getNotFoundPages().size());

        System.out.println("\nBrowser Statistics:");
        Map<String, Double> browserStats = getBrowserStatistics();
        for (Map.Entry<String, Double> entry : browserStats.entrySet()) {
            System.out.printf("  %-20s: %6.2f%%\n", entry.getKey(), entry.getValue() * 100);
        }

        System.out.println("\nResponse Code Statistics:");
        Map<Integer, Integer> responseStats = statistics.getResponseCodeStats();
        for (Map.Entry<Integer, Integer> entry : responseStats.entrySet()) {
            System.out.printf("  %3d: %d requests\n", entry.getKey(), entry.getValue());
        }
    }

    public Statistics getStatistics() {
        return statistics;
    }
}
