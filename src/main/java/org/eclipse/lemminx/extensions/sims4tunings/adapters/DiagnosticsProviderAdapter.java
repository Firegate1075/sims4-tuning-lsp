package org.eclipse.lemminx.extensions.sims4tunings.adapters;

import org.eclipse.lemminx.dom.DOMDocument;
import org.eclipse.lemminx.extensions.contentmodel.settings.XMLValidationSettings;
import org.eclipse.lemminx.extensions.sims4tunings.ITuningDescriptionObserver;
import org.eclipse.lemminx.extensions.sims4tunings.services.TuningDescriptionService;
import org.eclipse.lemminx.services.extensions.completion.ICompletionParticipant;
import org.eclipse.lemminx.services.extensions.diagnostics.IDiagnosticsParticipant;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

import java.util.List;

public class DiagnosticsProviderAdapter implements IDiagnosticsParticipant, ITuningDescriptionObserver {
    private boolean isActive = false;
    private final IDiagnosticsParticipant diagnosticsProvider;

    public DiagnosticsProviderAdapter(IDiagnosticsParticipant diagnosticsProvider) {
        TuningDescriptionService.getSingletonInstance().registerObserver(this);
        this.diagnosticsProvider = diagnosticsProvider;
    }

    @Override
    public void onTuningDescriptionInitialized() {
        if (TuningDescriptionService.getSingletonInstance().isInitialized()) {
            isActive = true;
        }
    }

    @Override
    public void doDiagnostics(DOMDocument xmlDocument, List<Diagnostic> diagnostics, XMLValidationSettings validationSettings, CancelChecker cancelChecker) {
        if (isActive) {
            diagnosticsProvider.doDiagnostics(xmlDocument, diagnostics, validationSettings, cancelChecker);
        }
    }
}
