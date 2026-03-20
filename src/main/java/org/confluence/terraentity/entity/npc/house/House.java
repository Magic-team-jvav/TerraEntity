package org.confluence.terraentity.entity.npc.house;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import org.confluence.terraentity.data.codec.TECodecs;
import org.confluence.terraentity.utils.AdapterUtils;

import java.util.Optional;
import java.util.UUID;

/**
 * 房子类,未来可能扩展不同的房子类型
 *
 * @param uuid   房子的uuid == 实体的uuid
 * @param min    左下角的坐标
 * @param max    右上角的坐标
 * @param center 房子的中心坐标
 */
public record House(Optional<UUID> uuid, BlockPos min, BlockPos max, BlockPos center) {
    public static final String KEY = "npc_house";
    public static final House EMPTY = new House(Optional.empty(), BlockPos.ZERO, BlockPos.ZERO, BlockPos.ZERO);

    public boolean isEmpty() {
        return uuid.isEmpty();
    }

    public boolean contains(BlockPos pos) {
        return pos.getX() >= min.getX() && pos.getX() <= max.getX() && pos.getZ() >= min.getZ() && pos.getZ() <= max.getZ();
    }

    public static final Codec<House> CODEC = RecordCodecBuilder.create((builder) -> builder.group(
            TECodecs.UUID_CODEC.optionalFieldOf("uuid").forGetter(House::uuid),
            BlockPos.CODEC.fieldOf("min").forGetter(House::min),
            BlockPos.CODEC.fieldOf("max").forGetter(House::max),
            BlockPos.CODEC.fieldOf("center").forGetter(House::center)
    ).apply(builder, House::new));

    public static FriendlyByteBuf.Writer<House> WRITER = AdapterUtils.CodecWriter(CODEC);
    public static FriendlyByteBuf.Reader<House> READER = AdapterUtils.CodecReader(CODEC);


}
