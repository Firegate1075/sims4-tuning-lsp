package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.extensions.sims4tunings.TuningDescriptionDataModel.TuningRoot;
import org.eclipse.lemminx.services.extensions.IXMLExtension;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.services.extensions.save.ISaveContext;
import org.eclipse.lsp4j.InitializeParams;

import java.util.List;
import java.util.logging.Logger;

public class Sims4TuningsLemminxExtension implements IXMLExtension {
    private RootElementCompletionProvider rootElementCompletionProvider;
    private TuningHashQuickFixProvider tuningHashQuickFixProvider;
    private TuningHashDiagnosticsProvider tuningHashDiagnosticsProvider;

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

        rootElementCompletionProvider = new RootElementCompletionProvider(tuningDescriptionRegistry);
        registry.registerCompletionParticipant(rootElementCompletionProvider);
        tuningHashQuickFixProvider = new TuningHashQuickFixProvider();
        registry.registerCodeActionParticipant(tuningHashQuickFixProvider);
        tuningHashDiagnosticsProvider = new TuningHashDiagnosticsProvider();
        registry.registerDiagnosticsParticipant(tuningHashDiagnosticsProvider);

        LOGGER.info("Sims4TuningsLemminxExtension initialized");
    }

    @Override
    public void stop(XMLExtensionsRegistry registry) {
        // Unregister here completion, hover, etc participants
        registry.unregisterCompletionParticipant(rootElementCompletionProvider);
        rootElementCompletionProvider = null;
        registry.unregisterCodeActionParticipant(tuningHashQuickFixProvider);
        tuningHashQuickFixProvider = null;
        registry.unregisterDiagnosticsParticipant(tuningHashDiagnosticsProvider);
        tuningHashDiagnosticsProvider = null;
    }
}
