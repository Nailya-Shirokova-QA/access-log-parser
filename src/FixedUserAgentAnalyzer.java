import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FixedUserAgentAnalyzer {
    private static final int MAX_LINE_LENGTH = 1024;

    private final Path filePath;
    private int totalLines;
    private int yandexBotCount;
    private int googleBotCount;
    private boolean analyzed;

    public FixedUserAgentAnalyzer(String filePath) {
        this.filePath = Paths.get(filePath);
        this.totalLines = 0;
        this.yandexBotCount = 0;
        this.googleBotCount = 0;
        this.analyzed = false;
    }

    public void analyze() {
        if (analyzed) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                totalLines++;

                if (line.length() > MAX_LINE_LENGTH) {
                    throw new LongLineException("Line too long: " + totalLines);
                }

                String lineLower = line.toLowerCase();

                if (lineLower.contains("yandexbot")) {
                    yandexBotCount++;
                }

                if (lineLower.contains("googlebot")) {
                    googleBotCount++;
                }
            }

            analyzed = true;
            printResults();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private void printResults() {
        System.out.println("=== Analysis Results ===");
        System.out.println("Total requests: " + totalLines);
        System.out.println("YandexBot requests: " + yandexBotCount);
        System.out.println("Googlebot requests: " + googleBotCount);

        double yandexPercent = totalLines > 0 ? (yandexBotCount * 100.0 / totalLines) : 0;
        double googlePercent = totalLines > 0 ? (googleBotCount * 100.0 / totalLines) : 0;

        System.out.printf("YandexBot percentage: %.2f%%\n", yandexPercent);
        System.out.printf("Googlebot percentage: %.2f%%\n", googlePercent);
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java FixedUserAgentAnalyzer <file>");
            return;
        }

        FixedUserAgentAnalyzer analyzer = new FixedUserAgentAnalyzer(args[0]);
        analyzer.analyze();
    }
}
