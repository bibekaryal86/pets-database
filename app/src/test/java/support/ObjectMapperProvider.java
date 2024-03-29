package support;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperProvider {
  /** for SonarLint */
  private ObjectMapperProvider() {
    throw new IllegalStateException("Utility class");
  }

  public static ObjectMapper objectMapper() {
    return new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
  }
}
