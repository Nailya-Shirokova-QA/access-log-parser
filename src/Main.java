import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        AccessLogParser parser = new AccessLogParser();
        String logFilePath = "access.log";

        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            int lineCount = 0;

            while ((line = reader.readLine()) != null) {
                lineCount++;
                parser.parseLogEntry(line);

                if (lineCount % 1000 == 0) {
                    System.out.println("Processed " + lineCount + " lines");
                }
            }

            System.out.println("Total lines: " + lineCount);
            System.out.println("Total requests: " + parser.getStatistics().getTotalRequests());

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return;
        }

        System.out.println("\n====== STATISTICS ======");
        parser.printStatistics();

        System.out.println("\n====== DETAILS ======");
        System.out.println("Existing pages count: " + parser.getExistingPages().size());

        Map<String, Double> osStats = parser.getOsStatistics();
        System.out.println("\nOperating Systems:");
        for (Map.Entry<String, Double> entry : osStats.entrySet()) {
            System.out.printf("%-15s: %.2f%%\n", entry.getKey(), entry.getValue() * 100);
        }

        printTopPages(parser.getExistingPages(), 10);
    }

    private static void printTopPages(List<String> pages, int limit) {
        System.out.println("\n====== TOP " + limit + " PAGES ======");
        int count = 0;
        for (String page : pages) {
            if (count >= limit) break;
            System.out.println(page);
            count++;
        }
    }
}

