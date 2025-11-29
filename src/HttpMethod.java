public enum HttpMethod {
    GET, POST, PUT, DELETE, HEAD, OPTIONS, PATCH, CONNECT, TRACE;

    public static HttpMethod fromString(String method) {
        try {
            return HttpMethod.valueOf(method.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
