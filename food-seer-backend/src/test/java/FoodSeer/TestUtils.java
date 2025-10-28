package FoodSeer;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for converting objects to JSON strings in tests.
 */
public class TestUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Converts an object to its JSON representation.
     *
     * @param obj The object to convert.
     * @return JSON string form of the object.
     */
    public static String asJsonString(final Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
