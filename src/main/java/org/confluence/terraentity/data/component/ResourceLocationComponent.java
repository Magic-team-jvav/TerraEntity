package org.confluence.terraentity.data.component;

import PortLib.extensions.net.minecraft.resources.ResourceLocation.PortResourceLocationExtension;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.mesdag.portlib.network.codec.PortStreamCodec;

import java.util.Objects;

public record ResourceLocationComponent(ResourceLocation location) {
    public static final Codec<ResourceLocationComponent> CODEC = ResourceLocation.CODEC.xmap(ResourceLocationComponent::new, ResourceLocationComponent::location);
    public static final PortStreamCodec<ByteBuf, ResourceLocationComponent> STREAM_CODEC = PortResourceLocationExtension.streamCodec()
            .map(ResourceLocationComponent::new, ResourceLocationComponent::location);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResourceLocationComponent that = (ResourceLocationComponent) o;
        return Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(location);
    }
}
