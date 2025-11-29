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
        System.out.println("Not found pages count: " + parser.getNotFoundPages().size());

        Map<String, Double> browserStats = parser.getBrowserStatistics();
        System.out.println("\nBrowsers:");
        for (Map.Entry<String, Double> entry : browserStats.entrySet()) {
            System.out.printf("%-20s: %.2f%%\n", entry.getKey(), entry.getValue() * 100);
        }

        printTopNotFoundPages(parser.getNotFoundPages(), 10);
    }

    private static void printTopNotFoundPages(List<String> pages, int limit) {
        System.out.println("\n====== TOP " + limit + " NOT FOUND PAGES ======");
        int count = 0;
        for (String page : pages) {
            if (count >= limit) break;
            System.out.println(page);
            count++;
        }
    }
}


