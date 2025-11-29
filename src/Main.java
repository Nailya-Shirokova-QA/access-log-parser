import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

        String testLog1 = "192.168.1.1 - - [01/Jan/2023:12:00:00 +0300] \"GET /index.html HTTP/1.1\" 200 1234 \"-\" \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36\"";
        String testLog2 = "192.168.1.1 - - [01/Jan/2023:14:00:00 +0300] \"GET /about.html HTTP/1.1\" 200 2000 \"-\" \"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36\"";

        LogEntry entry1 = new LogEntry(testLog1);
        LogEntry entry2 = new LogEntry(testLog2);

        System.out.println("=== Entry 1 ===");
        printEntry(entry1, outputFormatter);
        System.out.println("\n=== Entry 2 ===");
        printEntry(entry2, outputFormatter);

        Statistics stats = new Statistics();
        stats.addEntry(entry1);
        stats.addEntry(entry2);

        System.out.println("\n=== Statistics ===");
        System.out.printf("Traffic Rate: %.2f bytes/hour\n", stats.getTrafficRate());
    }

    private static void printEntry(LogEntry entry, DateTimeFormatter formatter) {
        System.out.println("IP: " + entry.getIpAddress());
        System.out.println("DateTime: " + entry.getDateTime().format(formatter));
        System.out.println("Method: " + entry.getHttpMethod());
        System.out.println("Path: " + entry.getPath());
        System.out.println("Response Code: " + entry.getResponseCode());
        System.out.println("Data Size: " + entry.getDataSize());
        System.out.println("Referer: " + entry.getReferer());
        System.out.println("Browser: " + entry.getUserAgent().getBrowser().getDisplayName());
        System.out.println("OS: " + entry.getUserAgent().getOperatingSystem().getDisplayName());
    }
}
