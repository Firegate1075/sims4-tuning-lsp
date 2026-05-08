package org.eclipse.lemminx.extensions.sims4tunings.adapters;

import org.eclipse.lemminx.extensions.sims4tunings.ITuningDescriptionObserver;
import org.eclipse.lemminx.extensions.sims4tunings.services.TuningDescriptionService;
import org.eclipse.lemminx.services.extensions.codeaction.ICodeActionParticipant;
import org.eclipse.lemminx.services.extensions.codeaction.ICodeActionRequest;
import org.eclipse.lemminx.services.extensions.codeaction.ICodeActionResolvesParticipant;
import org.eclipse.lemminx.services.extensions.diagnostics.IDiagnosticsParticipant;
import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

import java.util.List;
import java.util.concurrent.CancellationException;

public class QuickFixProviderAdapter implements ICodeActionParticipant, ITuningDescriptionObserver {
    private boolean isActive = false;
    private TuningDescriptionService tuningDescriptionService;
    private ICodeActionParticipant quickFixProvider;

    public QuickFixProviderAdapter(ICodeActionParticipant quickFixProvider, TuningDescriptionService tuningDescriptionService) {
        tuningDescriptionService.registerObserver(this);
        this.tuningDescriptionService = tuningDescriptionService;
        this.quickFixProvider = quickFixProvider;
    }

    @Override
    public void onTuningDescriptionInitialized() {
        if (tuningDescriptionService.isInitialized()) {
            isActive = true;
        }
    }

    @Override
    public void doCodeAction(ICodeActionRequest request, List<CodeAction> codeActions, CancelChecker cancelChecker) throws CancellationException {
        if (isActive) {
            quickFixProvider.doCodeAction(request, codeActions, cancelChecker);
        }
    }

    @Override
    public void doCodeActionUnconditional(ICodeActionRequest request, List<CodeAction> codeActions, CancelChecker cancelChecker) throws CancellationException {
        if (isActive) {
            quickFixProvider.doCodeActionUnconditional(request, codeActions, cancelChecker);
        }
    }

    @Override
    public ICodeActionResolvesParticipant getResolveCodeActionParticipant(String participantId) {
        if (isActive) {
            return quickFixProvider.getResolveCodeActionParticipant(participantId);
        } else {
            return null;
        }
    }
}
