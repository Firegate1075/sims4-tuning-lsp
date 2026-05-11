package org.eclipse.lemminx.extensions.sims4tunings.adapters;

import org.eclipse.lemminx.extensions.sims4tunings.ITuningDescriptionObserver;
import org.eclipse.lemminx.extensions.sims4tunings.services.TuningDescriptionService;
import org.eclipse.lemminx.services.extensions.completion.ICompletionParticipant;
import org.eclipse.lemminx.services.extensions.completion.ICompletionRequest;
import org.eclipse.lemminx.services.extensions.completion.ICompletionResponse;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

public class CompletionProviderAdapter implements ICompletionParticipant, ITuningDescriptionObserver {
    private boolean isActive = false;
    private final ICompletionParticipant completionProvider;

    public CompletionProviderAdapter(ICompletionParticipant completionProvider) {
        TuningDescriptionService.getSingletonInstance().registerObserver(this);
        this.completionProvider = completionProvider;
    }

    @Override
    public void onTagOpen(ICompletionRequest completionRequest, ICompletionResponse completionResponse, CancelChecker cancelChecker) throws Exception {
        if (isActive) {
            completionProvider.onTagOpen(completionRequest, completionResponse, cancelChecker);
        }
    }

    @Override
    public void onXMLContent(ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker) throws Exception {
        if (isActive) {
            completionProvider.onXMLContent(request, response, cancelChecker);
        }
    }

    @Override
    public void onAttributeName(boolean generateValue, ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker) throws Exception {
        if (isActive) {
            completionProvider.onAttributeName(generateValue, request, response, cancelChecker);
        }
    }

    @Override
    public void onAttributeValue(String valuePrefix, ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker) throws Exception {
        if (isActive) {
            completionProvider.onAttributeValue(valuePrefix, request, response, cancelChecker);
        }
    }

    @Override
    public void onDTDSystemId(String valuePrefix, ICompletionRequest request, ICompletionResponse response, CancelChecker cancelChecker) throws Exception {
        if (isActive) {
            completionProvider.onDTDSystemId(valuePrefix, request, response, cancelChecker);
        }
    }

    @Override
    public void onTuningDescriptionInitialized() {
        if (TuningDescriptionService.getSingletonInstance().isInitialized()) {
            isActive = true;
        }
    }
}
