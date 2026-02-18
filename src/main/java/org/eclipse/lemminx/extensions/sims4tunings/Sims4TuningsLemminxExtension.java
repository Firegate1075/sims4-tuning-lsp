package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel.TuningRoot;
import org.eclipse.lemminx.services.extensions.IXMLExtension;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.services.extensions.commands.IXMLCommandService;
import org.eclipse.lemminx.services.extensions.save.ISaveContext;
import org.eclipse.lsp4j.InitializeParams;

import java.util.List;
import java.util.logging.Logger;

public class Sims4TuningsLemminxExtension implements IXMLExtension {
    private TuningCompletionParticipant completionParticipant;

    private final static Logger LOGGER = Logger.getLogger(Sims4TuningsLemminxExtension.class.getName());

    @Override
    public void doSave(ISaveContext context) {
        // Called when settings or XML document are saved.
    }

    @Override
    public void start(InitializeParams params, XMLExtensionsRegistry registry) {
        // Register here completion, hover, etc participants

        // build tuning descriptions
        List<TuningRoot> tuningRoots = TuningDescriptionParser.parseTuningDescriptionXML();
        TuningDescriptionRegistry tuningDescriptionRegistry = TuningDescriptionRegistry.getInstance();
        tuningRoots.forEach(tuningDescriptionRegistry::addTuningDescription);

        completionParticipant = new TuningCompletionParticipant(tuningDescriptionRegistry);
        registry.registerCompletionParticipant(completionParticipant);

        LOGGER.info("Sims4TuningsLemminxExtension initialized");
    }

    @Override
    public void stop(XMLExtensionsRegistry registry) {
        // Unregister here completion, hover, etc participants
        registry.unregisterCompletionParticipant(completionParticipant);
    }
}
