package org.eclipse.lemminx.extensions.sims4tunings;

import org.eclipse.lemminx.services.extensions.IXMLExtension;
import org.eclipse.lemminx.services.extensions.XMLExtensionsRegistry;
import org.eclipse.lemminx.services.extensions.save.ISaveContext;
import org.eclipse.lsp4j.InitializeParams;

public class Sims4TuningsLemminxExtension implements IXMLExtension {
    @Override
    public void doSave(ISaveContext context) {
        // Called when settings or XML document are saved.
    }

    @Override
    public void start(InitializeParams params, XMLExtensionsRegistry registry) {
        // Register here completion, hover, etc participants
    }

    @Override
    public void stop(XMLExtensionsRegistry registry) {
        // Unregister here completion, hover, etc participants
    }
}
