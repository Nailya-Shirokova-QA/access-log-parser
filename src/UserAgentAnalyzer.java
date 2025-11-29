import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UserAgentAnalyzer {
    private static final int MAX_LINE_LENGTH = 1024;
    private static final String YANDEX_BOT = "YandexBot";
    private static final String GOOGLEBOT = "Googlebot";

    private final Path filePath;
    private int totalLines;
    private int yandexBotCount;
    private int googleBotCount;
    private boolean analyzed;

    public UserAgentAnalyzer(String filePath) {
        this.filePath = Paths.get(filePath);
        this.totalLines = 0;
        this.yandexBotCount = 0;
        this.googleBotCount = 0;
        this.analyzed = false;
    }

    private void validateFile() throws IOException {
        if (!Files.exists(filePath)) {
            throw new IOException("File does not exist: " + filePath);
        }

        if (!Files.isRegularFile(filePath)) {
            throw new IOException("Path is not a file: " + filePath);
        }
    }

    public void analyze() {
        if (analyzed) {
            return;
        }

        try {
            validateFile();

            try (FileReader fileReader = new FileReader(filePath.toFile());
                 BufferedReader reader = new BufferedReader(fileReader)) {
                String line;

                while ((line = reader.readLine()) != null) {
                    totalLines++;

                    if (line.length() > MAX_LINE_LENGTH) {
                        throw new LongLineException(
                                "Line " + totalLines + " exceeds maximum length " + MAX_LINE_LENGTH
                        );
                    }

                    analyzeUserAgent(line);
                }

                analyzed = true;

            } catch (LongLineException e) {
                throw e;
            } catch (IOException e) {
                throw new RuntimeException("Error reading file: " + e.getMessage(), e);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("File analysis interrupted", ex);
        }
    }

    private void analyzeUserAgent(String logLine) {
        try {
            String userAgentPart = extractUserAgentPart(logLine);

            if (userAgentPart != null) {
                String botName = extractBotName(userAgentPart);

                if (botName != null) {
                    if (YANDEX_BOT.equals(botName)) {
                        yandexBotCount++;
                    } else if (GOOGLEBOT.equals(botName)) {
                        googleBotCount++;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private String extractUserAgentPart(String logLine) {
        int firstQuote = logLine.indexOf('"');
        if (firstQuote == -1) return null;

        int secondQuote = logLine.indexOf('"', firstQuote + 1);
        if (secondQuote == -1) return null;

        int thirdQuote = logLine.indexOf('"', secondQuote + 1);
        if (thirdQuote == -1) return null;

        int fourthQuote = logLine.indexOf('"', thirdQuote + 1);
        if (fourthQuote == -1) return null;

        int fifthQuote = logLine.indexOf('"', fourthQuote + 1);
        if (fifthQuote == -1) return null;

        int sixthQuote = logLine.indexOf('"', fifthQuote + 1);
        if (sixthQuote == -1) return null;

        return logLine.substring(fifthQuote + 1, sixthQuote);
    }

    private String extractBotName(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return null;
        }

        String userAgentLower = userAgent.toLowerCase();

        if (userAgentLower.contains("yandexbot")) {
            return "YandexBot";
        }

        if (userAgentLower.contains("googlebot")) {
            return "Googlebot";
        }

        return null;
    }

    public int getTotalLines() {
        return totalLines;
    }

    public int getYandexBotCount() {
        return yandexBotCount;
    }

    public int getGoogleBotCount() {
        return googleBotCount;
    }

    public double getYandexBotPercentage() {
        if (totalLines == 0) return 0.0;
        return (yandexBotCount * 100.0) / totalLines;
    }

    public double getGoogleBotPercentage() {
        if (totalLines == 0) return 0.0;
        return (googleBotCount * 100.0) / totalLines;
    }

    public boolean isAnalyzed() {
        return analyzed;
    }

    public void printResults() {
        if (!analyzed) {
            System.out.println("File not analyzed yet. Call analyze() first.");
            return;
        }

        System.out.println("=== User Agent Analysis Results ===");
        System.out.println("Total requests: " + totalLines);
        System.out.println("YandexBot requests: " + yandexBotCount);
        System.out.println("Googlebot requests: " + googleBotCount);
        System.out.printf("YandexBot percentage: %.2f%%\n", getYandexBotPercentage());
        System.out.printf("Googlebot percentage: %.2f%%\n", getGoogleBotPercentage());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        UserAgentAnalyzer that = (UserAgentAnalyzer) obj;

        if (analyzed != that.analyzed) return false;
        if (analyzed) {
            if (totalLines != that.totalLines) return false;
            if (yandexBotCount != that.yandexBotCount) return false;
            if (googleBotCount != that.googleBotCount) return false;
        }

        return filePath.equals(that.filePath);
    }

    @Override
    public int hashCode() {
        int result = filePath.hashCode();
        result = 31 * result + (analyzed ? 1 : 0);
        if (analyzed) {
            result = 31 * result + totalLines;
            result = 31 * result + yandexBotCount;
            result = 31 * result + googleBotCount;
        }
        return result;
    }

    @Override
    public String toString() {
        if (!analyzed) {
            return "UserAgentAnalyzer{filePath=" + filePath + ", analyzed=false}";
        }

        return "UserAgentAnalyzer{" +
                "filePath=" + filePath +
                ", totalLines=" + totalLines +
                ", yandexBotCount=" + yandexBotCount +
                ", googleBotCount=" + googleBotCount +
                ", yandexBotPercentage=" + String.format("%.2f", getYandexBotPercentage()) + "%" +
                ", googleBotPercentage=" + String.format("%.2f", getGoogleBotPercentage()) + "%" +
                '}';
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify path to access.log file");
            System.out.println("Usage: java UserAgentAnalyzer <file_path>");
            return;
        }

        String filePath = args[0];
        UserAgentAnalyzer analyzer = new UserAgentAnalyzer(filePath);

        try {
            analyzer.analyze();
            analyzer.printResults();

            System.out.println("\n=== Testing equals() and hashCode() ===");
            UserAgentAnalyzer analyzer2 = new UserAgentAnalyzer(filePath);
            analyzer2.analyze();

            System.out.println("analyzer.equals(analyzer2): " + analyzer.equals(analyzer2));
            System.out.println("analyzer.hashCode() == analyzer2.hashCode(): " +
                    (analyzer.hashCode() == analyzer2.hashCode()));
            System.out.println("\nanalyzer.toString(): " + analyzer);

        } catch (LongLineException e) {
            System.err.println("Line too long detected:");
            System.err.println(e.getMessage());
            System.err.println("\nStatistics before interruption:");
            System.out.println("Total requests processed: " + analyzer.getTotalLines());
            System.out.println("YandexBot requests: " + analyzer.getYandexBotCount());
            System.out.println("Googlebot requests: " + analyzer.getGoogleBotCount());
        } catch (Exception e) {
            System.err.println("Error analyzing file: " + e.getMessage());
        }
    }
}
