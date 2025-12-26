package prodly.integration;

public class DataSerializer {
    public static String serialize(Object o) { return o == null ? "null" : o.toString(); }
}
