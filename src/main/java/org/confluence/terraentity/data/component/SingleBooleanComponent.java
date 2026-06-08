package org.confluence.terraentity.data.component;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import org.mesdag.portlib.network.codec.PortByteBufCodecs;
import org.mesdag.portlib.network.codec.PortStreamCodec;

public record SingleBooleanComponent(boolean value) {
    public static final SingleBooleanComponent TRUE = new SingleBooleanComponent(true);
    public static final SingleBooleanComponent FALSE = new SingleBooleanComponent(false);

    public static final Codec<SingleBooleanComponent> CODEC = Codec.BOOL.xmap(SingleBooleanComponent::new, SingleBooleanComponent::value);
    public static final PortStreamCodec<ByteBuf, SingleBooleanComponent> STREAM_CODEC = PortStreamCodec.composite(
            PortByteBufCodecs.BOOL, SingleBooleanComponent::value,
            SingleBooleanComponent::new
    );

    @Override
    public boolean equals(Object object) {
        return object instanceof SingleBooleanComponent c && c.value == value;
    }
}
