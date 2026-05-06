package org.eclipse.lemminx.extensions.sims4tunings.models.SettingsModel;

import com.google.gson.annotations.JsonAdapter;
import org.eclipse.lsp4j.jsonrpc.json.adapters.JsonElementTypeAdapter;

/**
 * Model class for the top level xml settings JSON object.
 */
public class RootSettings {

    @JsonAdapter(JsonElementTypeAdapter.Factory.class)
    private Object sims4tunings;

    public Object getSims4tunings() {
        return sims4tunings;
    }

    public void setSims4tunings(Object sims4tunings) {
        this.sims4tunings = sims4tunings;
    }
}
