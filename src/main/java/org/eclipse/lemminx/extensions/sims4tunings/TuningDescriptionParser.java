package org.eclipse.lemminx.extensions.sims4tunings;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel.TuningRoot;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;


public class TuningDescriptionParser {

    private static final String PROJECT_DIRECTORY = System.getProperty("user.dir");
    private static final Logger LOGGER = Logger.getLogger(TuningDescriptionParser.class.getName());

    public static void parseTuningDescriptionXML() {
        Path tdesc_path = Paths.get(PROJECT_DIRECTORY + "/tdesc");

        XmlMapper xmlMapper = new XmlMapper();

        try (Stream<Path> stream = Files.walk(tdesc_path)) {
            List<TuningRoot> parsedTuningDescriptionFiles = stream
                    .filter( path -> {
                        return path.toString().endsWith(".tdesc") || path.toString().endsWith(".tdescfrag");
                    })
                    .map(Path::toFile)
                    .map(file -> {
                        try {
                            return (Optional<TuningRoot>) Optional.of(xmlMapper.readValue(file, TuningRoot.class));
                        } catch (IOException e) {
                            LOGGER.severe("Failed to parse tdesc file " + file);
                            throw new RuntimeException(e.fillInStackTrace());
                            //return Optional.<TuningRoot>empty();
                        }
                    })
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

            System.out.println(parsedTuningDescriptionFiles);

        } catch (IOException e) {
            LOGGER.severe("Failed to open tdesc folder with path: " + tdesc_path);
            throw new RuntimeException(e.fillInStackTrace());
        }
    }

}
