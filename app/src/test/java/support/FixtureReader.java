package support;

import static java.lang.String.join;
import static java.nio.file.Files.readAllLines;
import static java.nio.file.Paths.get;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

public class FixtureReader {
  /** for SonarLint */
  private FixtureReader() {
    throw new IllegalStateException("Utility class");
  }

  public static String readFixture(String fileWhichExistsInResources) {
    // ../ exists because FixtureReader lives in support package
    URL url = FixtureReader.class.getResource("../" + fileWhichExistsInResources);

    try {
      if (url == null) {
        throw new FileNotFoundException(fileWhichExistsInResources);
      }

      Path path = get(url.toURI());
      return join("\n", readAllLines(path));
    } catch (IOException | URISyntaxException ex) {
      return null;
    }
  }
}
