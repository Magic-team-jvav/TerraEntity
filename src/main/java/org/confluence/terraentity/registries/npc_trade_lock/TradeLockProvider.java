package org.confluence.terraentity.registries.npc_trade_lock;

import com.github.edg_thexu.cafelib.data.codec.LazyVarMapCodecProvider;
import com.mojang.serialization.MapCodec;
import org.confluence.terraentity.api.npc.trade.ITradeLock;
import org.confluence.terraentity.api.npc.trade.TradeLockRecipeDrawer;

import java.util.function.Supplier;

/**
 * 用于提供NPC交易类型编解码器
 */
public class TradeLockProvider extends LazyVarMapCodecProvider<ITradeLock> {
    TradeLockRecipeDrawer drawer;
    public TradeLockProvider(Supplier<MapCodec<? extends ITradeLock>> mapCodecSupplier, TradeLockRecipeDrawer drawer) {
        super(mapCodecSupplier);
        this.drawer = drawer;
    }
    public TradeLockRecipeDrawer drawer() {
        return drawer;
    }
}
