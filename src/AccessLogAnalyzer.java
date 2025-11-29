import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AccessLogAnalyzer {
    private static final int MAX_LINE_LENGTH = 1024;
    private final Path filePath;
    private final FileStats fileStats;

    public AccessLogAnalyzer(String filePath) {
        this.filePath = Paths.get(filePath);
        this.fileStats = new FileStats();
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
        try {
            validateFile();

            try (FileReader fileReader = new FileReader(filePath.toFile());
                 BufferedReader reader = new BufferedReader(fileReader)) {

                String line;
                int lineNumber = 0;

                while ((line = reader.readLine()) != null) {
                    lineNumber++;
                    int length = line.length();

                    if (length > MAX_LINE_LENGTH) {
                        throw new LongLineException(
                                String.format("Line %d exceeds maximum length %d. " +
                                                "Line length: %d",
                                        lineNumber, MAX_LINE_LENGTH, length)
                        );
                    }

                    fileStats.addLine(length);
                }

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

    public FileStats getFileStats() {
        return fileStats;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please specify path to access.log file");
            System.out.println("Usage: java AccessLogAnalyzer <file_path>");
            return;
        }

        String filePath = args[0];
        AccessLogAnalyzer analyzer = new AccessLogAnalyzer(filePath);

        try {
            analyzer.analyze();
            FileStats stats = analyzer.getFileStats();
            System.out.println(stats);
        } catch (LongLineException e) {
            System.err.println("Line too long detected:");
            System.err.println(e.getMessage());
            System.err.println("\nStatistics before interruption:");
            System.err.println(analyzer.getFileStats());
        } catch (Exception e) {
            System.err.println("Error analyzing file: " + e.getMessage());
        }
    }
}