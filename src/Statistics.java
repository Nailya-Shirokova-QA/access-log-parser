import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {
    private Map<String, Boolean> existingPages;
    private Map<String, Integer> osFrequency;
    private int totalRequests;
    private Map<Integer, Integer> responseCodeStats;

    public Statistics() {
        this.existingPages = new HashMap<>();
        this.osFrequency = new HashMap<>();
        this.responseCodeStats = new HashMap<>();
        this.totalRequests = 0;
    }

    public void addEntry(LogEntry entry) {
        if (entry.getResponseCode() == 200) {
            existingPages.put(entry.getRequest(), true);
        }

        String os = detectOperatingSystem(entry.getUserAgent());
        osFrequency.put(os, osFrequency.getOrDefault(os, 0) + 1);

        int code = entry.getResponseCode();
        responseCodeStats.put(code, responseCodeStats.getOrDefault(code, 0) + 1);

        totalRequests++;
    }

    public List<String> getAllExistingPages() {
        return new ArrayList<>(existingPages.keySet());
    }

    public Map<String, Double> getOsStatistics() {
        Map<String, Double> osStatistics = new HashMap<>();

        for (Map.Entry<String, Integer> entry : osFrequency.entrySet()) {
            String os = entry.getKey();
            int count = entry.getValue();
            double proportion = (double) count / totalRequests;
            osStatistics.put(os, proportion);
        }

        return osStatistics;
    }

    private String detectOperatingSystem(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown";
        }

        String userAgentLower = userAgent.toLowerCase();

        if (userAgentLower.contains("windows")) {
            return "Windows";
        } else if (userAgentLower.contains("mac os") || userAgentLower.contains("macos")) {
            return "macOS";
        } else if (userAgentLower.contains("linux")) {
            return "Linux";
        } else if (userAgentLower.
                contains("android")) {
            return "Android";
        } else if (userAgentLower.contains("ios") || userAgentLower.contains("iphone")) {
            return "iOS";
        } else if (userAgentLower.contains("chrome os")) {
            return "ChromeOS";
        } else if (userAgentLower.contains("bsd")) {
            return "BSD";
        } else {
            return "Other";
        }
    }

    public void clearStatistics() {
        existingPages.clear();
        osFrequency.clear();
        responseCodeStats.clear();
        totalRequests = 0;
    }

    public Map<String, Boolean> getExistingPagesMap() {
        return new HashMap<>(existingPages);
    }

    public Map<String, Integer> getOsFrequency() {
        return new HashMap<>(osFrequency);
    }

    public Map<Integer, Integer> getResponseCodeStats() {
        return new HashMap<>(responseCodeStats);
    }

    public int getTotalRequests() {
        return totalRequests;
    }
}
