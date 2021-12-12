package support;

import com.fasterxml.jackson.databind.ObjectMapper;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class ObjectMapperProvider {
    /**
     * for SonarLint
     */
    private ObjectMapperProvider() {
        throw new IllegalStateException("Utility class");
    }

    public static ObjectMapper objectMapper() {
        return new ObjectMapper()
                .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
