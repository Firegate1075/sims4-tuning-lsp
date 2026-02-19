package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.services.extensions.completion.ICompletionParticipant;
import org.eclipse.lemminx.services.extensions.completion.ICompletionRequest;
import org.eclipse.lemminx.services.extensions.completion.ICompletionResponse;
import org.eclipse.lsp4j.jsonrpc.CancelChecker;

public class TuningDescriptionCompletionProvider implements ICompletionParticipant {
    // completion provider for tuning files based on the tuning description

    @Override
    public void onTagOpen(ICompletionRequest iCompletionRequest, ICompletionResponse iCompletionResponse, CancelChecker cancelChecker) throws Exception {

    }

    @Override
    public void onXMLContent(ICompletionRequest iCompletionRequest, ICompletionResponse iCompletionResponse, CancelChecker cancelChecker) throws Exception {

    }

    @Override
    public void onAttributeName(boolean b, ICompletionRequest iCompletionRequest, ICompletionResponse iCompletionResponse, CancelChecker cancelChecker) throws Exception {

    }

    @Override
    public void onAttributeValue(String s, ICompletionRequest iCompletionRequest, ICompletionResponse iCompletionResponse, CancelChecker cancelChecker) throws Exception {

    }

    @Override
    public void onDTDSystemId(String s, ICompletionRequest iCompletionRequest, ICompletionResponse iCompletionResponse, CancelChecker cancelChecker) throws Exception {

    }
}
