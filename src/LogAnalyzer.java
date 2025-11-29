import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

public class LogAnalyzer {
    private final Path filePath;
    private final Statistics statistics;
    private int parsedCount = 0;

    public LogAnalyzer(String filePath) {
        this.filePath = Paths.get(filePath);
        this.statistics = new Statistics();
    }

    private void validateFile() throws IOException {
        if (!Files.exists(filePath)) {
            throw new IOException("File not found: " + filePath);
        }
        if (!Files.isRegularFile(filePath)) {
            throw new IOException("Not a regular file: " + filePath);
        }
        if (!Files.isReadable(filePath)) {
            throw new IOException("File not readable: " + filePath);
        }
    }

    public void analyze() throws IOException {
        validateFile();

        int lineCount = 0;
        int errorCount = 0;

        System.out.println("Starting log analysis");
        System.out.println("File: " + filePath);
        System.out.println();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                lineCount++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    LogEntry entry = new LogEntry(line);
                    statistics.addEntry(entry);
                    parsedCount++;

                    if (parsedCount <= 5) {
                        printEntrySummary(entry, parsedCount);
                    }

                } catch (Exception e) {
                    errorCount++;
                    if (errorCount <= 3) {
                        System.err.println("Parse error line " + lineCount + ": " + e.getMessage());
                    }
                }
            }
        }

        printAnalysisSummary(lineCount, errorCount);
        printStatistics();
    }

    private void printEntrySummary(LogEntry entry, int index) {
        System.out.println("[" + index + "] " +
                entry.getIpAddress() + " | " +
                entry.getDateTime().toString().replace("T", " ") + " | " +
                entry.getHttpMethod() + " " + entry.getPath() + " | " +
                entry.getResponseCode() + " | " +
                entry.getDataSize() + " bytes | " +
                entry.getUserAgent().getBrowser().getDisplayName() + " on " +
                entry.getUserAgent().getOperatingSystem().getDisplayName());
    }

    private void printAnalysisSummary(int totalLines, int errors) {
        System.out.println();
        System.out.println("Analysis Summary");
        System.out.println("Total lines: " + totalLines);
        System.out.println("Successfully parsed: " + parsedCount);
        System.out.println("Parse errors: " + errors);
    }

    private void printStatistics() {
        System.out.println();
        System.out.println("Traffic Statistics");
        System.out.println("Total requests: " + parsedCount);
        System.out.println("Total traffic: " + statistics.getTotalTraffic() + " bytes");

        if (statistics.getMinTime() != null && statistics.getMaxTime() != null) {
            System.out.println("Time range: " + statistics.getMinTime() + " to " + statistics.getMaxTime());
            long hours = Duration.between(statistics.getMinTime(), statistics.getMaxTime()).toHours();
            System.out.println("Duration: " + hours + " hours");
            System.out.printf("Average traffic rate: %.2f bytes/hour%n", statistics.getTrafficRate());
        } else {
            System.out.println("No time data available");
        }
    }

    public Statistics getStatistics() {
        return statistics;
    }
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java LogAnalyzer <log-file-path>");
            System.out.println("Example: java LogAnalyzer access.log");
            return;
        }

        String filePath = args[0];
        LogAnalyzer analyzer = new LogAnalyzer(filePath);

        try {
            analyzer.analyze();
        } catch (IOException e) {
            System.err.println("Error analyzing file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }
}
