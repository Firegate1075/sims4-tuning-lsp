package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.extensions.sims4tunings.adapters.CompletionProviderAdapter;
import org.eclipse.lemminx.extensions.sims4tunings.adapters.DiagnosticsProviderAdapter;
import org.eclipse.lemminx.extensions.sims4tunings.adapters.QuickFixProviderAdapter;
import org.eclipse.lemminx.extensions.sims4tunings.models.TuningDescriptionDataModel.TuningRoot;
import org.eclipse.lemminx.extensions.sims4tunings.services.SettingsService;
import org.eclipse.lemminx.extensions.sims4tunings.services.TuningDescriptionService;
import org.eclipse.lemminx.services.extensions.IXMLExtension;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.services.extensions.save.ISaveContext;
import org.eclipse.lsp4j.InitializeParams;

import java.util.List;
import java.util.logging.Logger;

public class Sims4TuningsLemminxExtension implements IXMLExtension {
    private CompletionProviderAdapter rootElementCompletionProviderAdapter;
    private QuickFixProviderAdapter tuningHashQuickFixProviderAdapter;
    private DiagnosticsProviderAdapter tuningHashDiagnosticsProviderAdapter;
    private CompletionProviderAdapter tuningDescriptionCompletionProviderAdapter;
    private SettingsService settingsService;

    private final static Logger LOGGER = Logger.getLogger(Sims4TuningsLemminxExtension.class.getName());

    @Override
    public void doSave(ISaveContext context) {
        // Called when settings or XML document are saved.
        if (context.getType() == ISaveContext.SaveContextType.SETTINGS) {
            // handle save of settings
            SettingsService.getInstance().updateSettings(context.getSettings());
            LOGGER.info("Settings saved");
        }
    }

    @Override
    public void start(InitializeParams params, XMLExtensionsRegistry registry) {
        // Register here completion, hover, etc participants

        // build tuning descriptions
        TuningDescriptionRegistry tuningDescriptionRegistry = new TuningDescriptionRegistry();
        TuningDescriptionService tuningDescriptionService = new TuningDescriptionService(tuningDescriptionRegistry);

        RootElementCompletionProvider rootElementCompletionProvider = new RootElementCompletionProvider(tuningDescriptionService);
        rootElementCompletionProviderAdapter = new CompletionProviderAdapter(rootElementCompletionProvider, tuningDescriptionService);
        registry.registerCompletionParticipant(rootElementCompletionProviderAdapter);

        TuningHashQuickFixProvider tuningHashQuickFixProvider = new TuningHashQuickFixProvider();
        tuningHashQuickFixProviderAdapter = new QuickFixProviderAdapter(tuningHashQuickFixProvider,  tuningDescriptionService);
        registry.registerCodeActionParticipant(tuningHashQuickFixProviderAdapter);

        TuningHashDiagnosticsProvider tuningHashDiagnosticsProvider = new TuningHashDiagnosticsProvider();
        tuningHashDiagnosticsProviderAdapter = new DiagnosticsProviderAdapter(tuningHashDiagnosticsProvider, tuningDescriptionService);
        registry.registerDiagnosticsParticipant(tuningHashDiagnosticsProviderAdapter);

        TuningDescriptionCompletionProvider tuningDescriptionCompletionProvider = new TuningDescriptionCompletionProvider(tuningDescriptionService);
        tuningDescriptionCompletionProviderAdapter = new CompletionProviderAdapter(tuningDescriptionCompletionProvider, tuningDescriptionService);
        registry.registerCompletionParticipant(tuningDescriptionCompletionProviderAdapter);

        LOGGER.info("Sims4TuningsLemminxExtension initialized");
    }

    @Override
    public void stop(XMLExtensionsRegistry registry) {
        // Unregister here completion, hover, etc. participants
        registry.unregisterCompletionParticipant(rootElementCompletionProviderAdapter);
        rootElementCompletionProviderAdapter = null;
        registry.unregisterCodeActionParticipant(tuningHashQuickFixProviderAdapter);
        tuningHashQuickFixProviderAdapter = null;
        registry.unregisterDiagnosticsParticipant(tuningHashDiagnosticsProviderAdapter);
        tuningHashDiagnosticsProviderAdapter = null;
        registry.unregisterCompletionParticipant(tuningDescriptionCompletionProviderAdapter);
        tuningDescriptionCompletionProviderAdapter = null;
    }
}
