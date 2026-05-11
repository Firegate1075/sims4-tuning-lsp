package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.extensions.sims4tunings.adapters.CompletionProviderAdapter;
import org.eclipse.lemminx.extensions.sims4tunings.adapters.DiagnosticsProviderAdapter;
import org.eclipse.lemminx.extensions.sims4tunings.adapters.QuickFixProviderAdapter;
import org.eclipse.lemminx.extensions.sims4tunings.providers.RootElementCompletionProvider;
import org.eclipse.lemminx.extensions.sims4tunings.providers.TuningDescriptionCompletionProvider;
import org.eclipse.lemminx.extensions.sims4tunings.providers.TuningHashDiagnosticsProvider;
import org.eclipse.lemminx.extensions.sims4tunings.providers.TuningHashQuickFixProvider;
import org.eclipse.lemminx.extensions.sims4tunings.repository.TuningDescriptionRegistry;
import org.eclipse.lemminx.extensions.sims4tunings.services.SettingsService;
import org.eclipse.lemminx.extensions.sims4tunings.services.TuningDescriptionService;
import org.eclipse.lemminx.services.extensions.IXMLExtension;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.services.extensions.save.ISaveContext;
import org.eclipse.lsp4j.InitializeParams;

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
        LOGGER.info("Saving " + context.getType());
        if (context.getType() == ISaveContext.SaveContextType.SETTINGS) {
            // handle save of settings
            SettingsService.getSingletonInstance().updateSettings(context.getSettings());
            LOGGER.info("Settings saved");
        }
    }

    @Override
    public void start(InitializeParams params, XMLExtensionsRegistry registry) {

        TuningDescriptionRegistry tuningDescriptionRegistry = new TuningDescriptionRegistry();
        TuningDescriptionService.createSingletonInstance(tuningDescriptionRegistry);

        // Register here completion, hover, etc participants

        RootElementCompletionProvider rootElementCompletionProvider = new RootElementCompletionProvider();
        rootElementCompletionProviderAdapter = new CompletionProviderAdapter(rootElementCompletionProvider);
        registry.registerCompletionParticipant(rootElementCompletionProviderAdapter);

        TuningHashQuickFixProvider tuningHashQuickFixProvider = new TuningHashQuickFixProvider();
        tuningHashQuickFixProviderAdapter = new QuickFixProviderAdapter(tuningHashQuickFixProvider);
        registry.registerCodeActionParticipant(tuningHashQuickFixProviderAdapter);

        TuningHashDiagnosticsProvider tuningHashDiagnosticsProvider = new TuningHashDiagnosticsProvider();
        tuningHashDiagnosticsProviderAdapter = new DiagnosticsProviderAdapter(tuningHashDiagnosticsProvider);
        registry.registerDiagnosticsParticipant(tuningHashDiagnosticsProviderAdapter);

        TuningDescriptionCompletionProvider tuningDescriptionCompletionProvider = new TuningDescriptionCompletionProvider();
        tuningDescriptionCompletionProviderAdapter = new CompletionProviderAdapter(tuningDescriptionCompletionProvider);
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
