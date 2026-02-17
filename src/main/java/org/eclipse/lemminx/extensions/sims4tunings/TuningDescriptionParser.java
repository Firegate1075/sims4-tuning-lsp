package org.eclipse.lemminx.extensions.sims4tunings;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel.TuningRoot;

import java.io.IOException;
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

    public static List<TuningRoot> parseTuningDescriptionXML() {
        Path tdesc_path = Paths.get(PROJECT_DIRECTORY + "/tdesc");

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(TuningRoot.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            try (Stream<Path> stream = Files.walk(tdesc_path)) {

                return stream
                        .filter(path -> {
                            return path.toString().endsWith(".tdesc") || path.toString().endsWith(".tdescfrag");
                        })
                        .map(path -> {
                            try {
                                return (TuningRoot) unmarshaller.unmarshal(path.toFile());
                            } catch (JAXBException e) {
                                LOGGER.severe("Failed to parse tdesc file " + path);
                                throw new RuntimeException(e.fillInStackTrace());
                            }
                        })
                        .toList();

            } catch (IOException e) {
                LOGGER.severe("Failed to open tdesc folder with path: " + tdesc_path);
                throw new RuntimeException(e.fillInStackTrace());
            }
        } catch (JAXBException e) {
            LOGGER.severe("Failed to create JAXB context");
            throw new RuntimeException(e.fillInStackTrace());
        }
    }

}
