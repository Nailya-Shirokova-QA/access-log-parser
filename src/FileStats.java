public class FileStats {
    private int totalLines;
    private int longestLineLength;
    private int shortestLineLength;

    public FileStats() {
        this.totalLines = 0;
        this.longestLineLength = 0;
        this.shortestLineLength = Integer.MAX_VALUE;
    }

    public int getTotalLines() {
        return totalLines;
    }

    public int getLongestLineLength() {
        return longestLineLength;
    }

    public int getShortestLineLength() {
        return shortestLineLength;
    }

    public void addLine(int lineLength) {
        totalLines++;

        if (lineLength > longestLineLength) {
            longestLineLength = lineLength;
        }

        if (lineLength < shortestLineLength) {
            shortestLineLength = lineLength;
        }
    }

    public void reset() {
        totalLines = 0;
        longestLineLength = 0;
        shortestLineLength = Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        int shortest = shortestLineLength == Integer.MAX_VALUE ? 0 : shortestLineLength;
        return "Total lines: " + totalLines + "\n" +
                "Longest line length: " + longestLineLength + "\n" +
                "Shortest line length: " + shortest + "\n";
    }
}
