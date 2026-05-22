> **AI-generated** — 2026-05-22

# Configuration Files (配置文件)

TerraEntity uses NeoForge `ModConfigSpec` and custom JSON config files.

---

## Server Config (`terra_entity-server.toml`)

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| `boss_clear_when_no_target` | boolean | `true` | Clear boss when no target found |
| `boss_attributes_multiplier_health` | double | `1.0` | Boss health multiplier (0.0625–10) |
| `boss_attributes_multiplier_damage` | double | `1.0` | Boss damage multiplier (0.0625–10) |
| `boss_no_physics` | boolean | `true` | Boss has no physics collision |
| `boss_leave_on_day` | boolean | `false` | Boss leaves during daytime |
| `boss_keep_wandering` | boolean | `true` | Boss random strolls when no target |
| `enhance_all_monster` | boolean | `false` | Apply enhancements to all monsters |
| `monster_attributes_multiplier_health` | double | `1.0` | Monster health multiplier (0.0625–100) |
| `monster_attributes_multiplier_damage` | double | `1.0` | Monster damage multiplier (0.0625–100) |
| `spawn_without_light` | boolean | `true` | Monsters spawn regardless of light level |
| `chance_to_spawn_slime_on_zombie_head` | double | `0.05` | Chance for slime on zombie head (0–1) |
| `enemy_spawn_chance` | double | `1.0` | Global monster spawn chance (0–1) |
| `enemy_spawn_chance_apply_all` | boolean | `false` | Apply spawn chance to all mods' monsters |
| `behavior_tree_web_viewer_server_port` | int | `59160` | Behavior tree debug port (1024–65535) |

---

## Client Config (`terra_entity-client.toml`)

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| `boss_bar_style` | int | `1` | 0=Default, 1=Still, 2=Dynamic |
| `boss_bar_number_offset_x` | int | `0` | Boss bar number X offset (-150–300) |
| `boss_bar_number_offset_y` | int | `0` | Boss bar number Y offset (-30–200) |
| `generate_projectile_particle` | boolean | `true` | Toggle projectile particles |
| `enableNonSpiderModel` | boolean | `false` | (internal) |
| `npc_chat_bubble_style` | enum | `RECT` | RECT or CLOUD |
| `enable_entity_motion_blur` | boolean | `true` | Toggle motion blur effect |

---

## Attribute Config (`config/terra_entity/attribute_config.json`)

JSON config for overriding monster attributes. Highest priority (overrides KubeJS and defaults).

```json
{
  "settings": {
    "terra_entity:base_bones": [
      { "attribute": "minecraft:generic.movement_speed", "amount": 1.0 },
      { "attribute": "lib:attack_damage", "amount": 1.0 },
      { "attribute": "minecraft:generic.max_health", "amount": 1.0 }
    ]
  }
}
```

Format: Map of `EntityType → List<{attribute, amount}>`.

---

## Data-Driven Configs (Datapack)

These are loaded from datapacks and can be overridden:

| Config | Path | Content |
|--------|------|---------|
| Boss Skill Params | `terra_entity/boss_skill_map_data/<boss_key>` | Boss AI parameters (damage, speed, cooldowns, etc.) |
| NPC Shops | `terra_entity/npc/shop/<npc_id>.json` | NPC trade lists |
| NPC Moods | `terra_entity/npc/moods.json` | Inter-NPC mood relationships |
| NPC Chat | `terra_entity/npc/chat/<npc_id>.json` | Timed chat triggers |
| NPC Names | `terra_entity/npc/names.json` | Random NPC name lists |
| Initial Weapons | `terra_entity/npc_initial_weapon` | NPC starting weapons |
| Whip Path | `terra_entity/whip_path_config` | Custom whip swing animations |
| Curios Slots | `terra_entity/curios/slots/` | Pet, mount, light pet slots |
