> **AI-generated** — 2026-05-22

# Boomerangs (回旋镖)

Boomerangs are thrown ranged weapons that fly forward and return to the player. Multi-boomerangs can have multiple projectiles active at once.

Key stats per boomerang:
- **Damage:** per hit
- **Forward Tick:** how long the boomerang flies before returning (ticks)
- **Speed Factor:** flight speed multiplier (forward / return)
- **Durability:** number of uses before break

| Name | Damage | Forward | Speed (Fwd/Back) | Durability | Range | Special Effect |
|------|--------|---------|-----------------|------------|-------|---------------|
| Wood Boomerang (`wood_boomerang`) | 3.8 | — | — | 100 | Short | — |
| Enchanted Boomerang (`enchanted_boomerang`) | 6.6 | 15 | 1.55/1.55 | 300 | Medium | Glowing (luminance 5) |
| Shroomerang (`shroomerang`) | 8.6 | 15 | 1.55/1.55 | 500 | Medium | Glowing (luminance 5) |
| Ice Boomerang (`ice_boomerang`) | 8.3 | 16 | 1.6/1.6 | 500 | Medium | Frostburn (50% chance, 3s), snow particles |
| Trimarang (`trimarang`) | 8.3×3 | 17 | 1.85/1.85 | 1000 | Medium | 3 simultaneous, cooldown 10 |
| Combat Wrench (`combat_wrench`) | 9.0 | 10 | 3.0/1.85 | 1500 | Medium | — |
| Flamarang (`flamarang`) | 20.3 | 18 | 1.85/1.85 | 1500 | Long | Hellfire, fire particles |
| BeiDou Boomerang (`bei_dou_boomerang`) | 5×4 | 40 | 3.0/3.0 | — | Long | 4 projectiles, 7 penetration, special hit effect |
| Developer Boomerang (`developer_boomerang`) | 9999 | 50 | 2.0/2.0 | — | Very Long | 10 projectiles, unbreakable, penetrates, no cooldown |

**Mechanics:**
- Left-click to throw; boomerang flies forward, then returns
- **Normal:** must wait for return before next throw
- **Multi (Trimarang, BeiDou, Developer):** multiple active at once with cooldown
- Can be enchanted with Multi Boomerang enchant for extra projectiles
- Status effects on hit (frostburn, hellfire, etc.)
- Custom particle trails (snow, fire, colored dust)
