package org.confluence.terraentity.init;

import org.confluence.lib.common.PlayerContainer;
import org.confluence.terraentity.TerraEntity;
import org.confluence.terraentity.attachment.ItemInHandTrailAttachment;
import org.confluence.terraentity.attachment.SummonerAttachment;
import org.confluence.terraentity.attachment.UnSyncableAttachment;
import org.confluence.terraentity.attachment.WeaponStorage;
import org.confluence.terraentity.utils.DriveAwaySystem.DriveAwayAttachment;
import org.confluence.terraentity.utils.DriveAwaySystem.DriveAwayAttachment.DriveAwayData;
import org.mesdag.portlib.attachment.PortAttachmentType;
import org.mesdag.portlib.registries.PortAttachmentRegistration;
import org.mesdag.portlib.registries.PortRegisterHandler;
import org.mesdag.portlib.registries.PortRegistryEntry;

public final class TEAttachments {
    public static final PortAttachmentRegistration TYPES = PortRegisterHandler.attachment(TerraEntity.MODID);

    public static final PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<SummonerAttachment>> SUMMONER_STORAGE = TYPES.register("summoner_storage", () -> PortAttachmentType.serializable(ins -> new SummonerAttachment(SummonerAttachment.SummonerType.MINION)).copyOnDeath().build());
    public static final PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<SummonerAttachment>> SENTRY_STORAGE = TYPES.register("sentry_storage", () -> PortAttachmentType.serializable(ins -> new SummonerAttachment(SummonerAttachment.SummonerType.SENTRY)).copyOnDeath().build());
    public static final PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<WeaponStorage>> WEAPON_STORAGE = TYPES.register("weapon_storage", () -> PortAttachmentType.serializable(WeaponStorage::new).copyOnDeath().build());
    public static final PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<ItemInHandTrailAttachment>> TRAIL_STORAGE = TYPES.register("trail_storage", () -> PortAttachmentType.serializable(ItemInHandTrailAttachment::new).build());
    public static final PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<PlayerContainer>> CHESTER = TYPES.register("chester", () -> PortAttachmentType.serializable(() -> new PlayerContainer(6)).copyOnDeath().build());
    public static final PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<UnSyncableAttachment>> UNSYNC = TYPES.register("unsync", () -> PortAttachmentType.serializable(() -> new UnSyncableAttachment()).build());
    public static final PortRegistryEntry<PortAttachmentType<?>, PortAttachmentType<DriveAwayData>> DRIVE_AWAY_DATA = TYPES.register("drive_away_data", () -> PortAttachmentType.builder(DriveAwayData::new).serialize(DriveAwayAttachment.DriveAwayData.CODEC).build());
}
