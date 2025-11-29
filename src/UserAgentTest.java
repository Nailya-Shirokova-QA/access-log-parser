public class UserAgentTest {
    public static void main(String[] args) {
        System.out.println("=== Testing equals(), hashCode() and toString() methods ===\n");

        System.out.println("Test 1: Create two analyzers for same file");
        UserAgentAnalyzer analyzer1 = new UserAgentAnalyzer("access.log");
        UserAgentAnalyzer analyzer2 = new UserAgentAnalyzer("access.log");

        System.out.println("Before analysis:");
        System.out.println("analyzer1.equals(analyzer2): " + analyzer1.equals(analyzer2));
        System.out.println("analyzer1.hashCode(): " + analyzer1.hashCode());
        System.out.println("analyzer2.hashCode(): " + analyzer2.hashCode());
        System.out.println("analyzer1.toString(): " + analyzer1);

        System.out.println("\nTest 2: Analyze files (mock analysis)");
        analyzer1 = createTestAnalyzer("access.log", 1000, 150, 200);
        analyzer2 = createTestAnalyzer("access.log", 1000, 150, 200);

        System.out.println("After analysis:");
        System.out.println("analyzer1.equals(analyzer2): " + analyzer1.equals(analyzer2));
        System.out.println("analyzer1.hashCode() == analyzer2.hashCode(): " +
                (analyzer1.hashCode() == analyzer2.hashCode()));
        System.out.println("analyzer1.toString(): " + analyzer1);

        System.out.println("\nTest 3: Different file");
        UserAgentAnalyzer analyzer3 = createTestAnalyzer("other.log", 500, 50, 75);
        System.out.println("analyzer1.equals(analyzer3): " + analyzer1.equals(analyzer3));

        System.out.println("\nTest 4: Different analysis results");
        UserAgentAnalyzer analyzer4 = createTestAnalyzer("access.log", 1000, 100, 200);
        System.out.println("analyzer1.equals(analyzer4): " + analyzer1.equals(analyzer4));

        System.out.println("\nTest 5: Special cases");
        System.out.println("analyzer1.equals(null): " + analyzer1.equals(null));
        System.out.println("analyzer1.equals(\"string\"): " + analyzer1.equals("string"));
        System.out.println("analyzer1.equals(analyzer1): " + analyzer1.equals(analyzer1));

        System.out.println("\nTest 6: hashCode() consistency");
        System.out.println("analyzer1.hashCode(): " + analyzer1.hashCode());
        System.out.println("analyzer2.hashCode(): " + analyzer2.hashCode());
        System.out.println("analyzer3.hashCode(): " + analyzer3.hashCode());
        System.out.println("analyzer4.hashCode(): " + analyzer4.hashCode());

        System.out.println("\nTest 7: toString() examples");
        System.out.println("Not analyzed analyzer: " + new UserAgentAnalyzer("test.log"));
        System.out.println("Analyzed analyzer: " + analyzer1);
    }

    private static UserAgentAnalyzer createTestAnalyzer(String path, int total, int yandex, int google) {
        return new UserAgentAnalyzer(path) {
            {
                try {
                    java.lang.reflect.Field totalLinesField = UserAgentAnalyzer.class.getDeclaredField("totalLines");
                    totalLinesField.setAccessible(true);
                    totalLinesField.set(this, total);

                    java.lang.reflect.Field yandexField = UserAgentAnalyzer.class.getDeclaredField("yandexBotCount");
                    yandexField.setAccessible(true);
                    yandexField.set(this, yandex);

                    java.lang.reflect.Field googleField = UserAgentAnalyzer.class.getDeclaredField("googleBotCount");
                    googleField.setAccessible(true);
                    googleField.set(this, google);

                    java.lang.reflect.Field analyzedField = UserAgentAnalyzer.class.getDeclaredField("analyzed");
                    analyzedField.setAccessible(true);
                    analyzedField.set(this, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
