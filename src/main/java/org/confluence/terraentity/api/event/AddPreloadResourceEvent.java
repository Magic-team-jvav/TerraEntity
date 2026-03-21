package org.confluence.terraentity.api.event;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;
import java.util.List;

public class AddPreloadResourceEvent extends Event {
    private final List<PreparableReloadListener> listeners;
    public AddPreloadResourceEvent(){
        this.listeners = new ArrayList<>();
    }
    public AddPreloadResourceEvent add(PreparableReloadListener listener) {
        this.listeners.add(listener);
        return this;
    }

    public List<PreparableReloadListener> getListeners() {
        return listeners;
    }
}
