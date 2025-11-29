import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Statistics {
    private Set<String> notFoundPages;
    private Map<String, Integer> browserFrequency;
    private int totalRequests;
    private Map<Integer, Integer> responseCodeStats;

    public Statistics() {
        this.notFoundPages = new HashSet<>();
        this.browserFrequency = new HashMap<>();
        this.responseCodeStats = new HashMap<>();
        this.totalRequests = 0;
    }

    public void addEntry(LogEntry entry) {
        if (entry.getResponseCode() == 404) {
            notFoundPages.add(entry.getRequest());
        }

        String browser = detectBrowser(entry.getUserAgent());

        if (browserFrequency.containsKey(browser)) {
            int currentCount = browserFrequency.get(browser);
            browserFrequency.put(browser, currentCount + 1);
        } else {
            browserFrequency.put(browser, 1);
        }

        int code = entry.getResponseCode();

        if (responseCodeStats.containsKey(code)) {
            int currentCount = responseCodeStats.get(code);
            responseCodeStats.put(code, currentCount + 1);
        } else {
            responseCodeStats.put(code, 1);
        }

        totalRequests++;
    }

    public List<String> getAllNotFoundPages() {
        return new ArrayList<>(notFoundPages);
    }

    public Map<String, Double> getBrowserStatistics() {
        Map<String, Double> browserStatistics = new HashMap<>();

        for (Map.Entry<String, Integer> entry : browserFrequency.entrySet()) {
            String browser = entry.getKey();
            int count = entry.getValue();
            double proportion = (double) count / totalRequests;
            browserStatistics.put(browser, proportion);
        }

        return browserStatistics;
    }

    private String detectBrowser(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return "Unknown";
        }

        String userAgentLower = userAgent.toLowerCase();

        if (userAgentLower.contains("chrome") && !userAgentLower.contains("chromium")) {
            return "Chrome";
        } else if (userAgentLower.contains("firefox")) {
            return "Firefox";
        } else if (userAgentLower.contains("safari") && !userAgentLower.contains("chrome")) {
            return "Safari";
        } else if (userAgentLower.contains("edge")) {
            return "Edge";
        } else if (userAgentLower.contains("opera")) {
            return "Opera";
        } else if (userAgentLower.contains("ie") || userAgentLower.contains("internet explorer")) {
            return "Internet Explorer";
        } else if (userAgentLower.contains("chromium")) {
            return "Chromium";
        } else if (userAgentLower.contains("curl")) {
            return "cURL";
        } else if (userAgentLower.contains("wget")) {
            return "Wget";
        } else if (userAgentLower.contains("bot") || userAgentLower.contains("crawler") || userAgentLower.contains("spider")) {
            return "Bot/Crawler";
        } else {
            return "Other";
        }
    }

    public void clearStatistics() {
        notFoundPages.clear();
        browserFrequency.clear();
        responseCodeStats.clear();
        totalRequests = 0;
    }

    public Set<String> getNotFoundPagesSet() {
        return new HashSet<>(notFoundPages);
    }

    public Map<String, Integer> getBrowserFrequency() {
        return new HashMap<>(browserFrequency);
    }

    public Map<Integer, Integer> getResponseCodeStats() {
        return new HashMap<>(responseCodeStats);
    }

    public int getTotalRequests() {
        return totalRequests;
    }
}
