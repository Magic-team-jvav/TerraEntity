> **AI-generated** — 2026-05-22

# Whips (鞭子)

Whips are summoner weapons that deal damage and apply a **summon tag** (mark damage). Minions prioritize tagged targets and deal bonus damage. Each whip has a unique range and attack speed.

Key stats:
- **Damage:** base summon damage
- **Mark Damage:** bonus damage applied to minion attacks on tagged targets
- **Attack Speed:** modifier to base attack speed
- **Cooldown:** ticks between hits on the same target
- **Range Factor:** multiplier for whip range

| Name | Damage | Mark | Speed | Cooldown | Range | Durability | Special |
|------|--------|------|-------|----------|-------|------------|---------|
| Leather Whip (`leather_whip`) | 10 | 1 | 0.5 | 15 | 0.9 | 200 | Grants Strength on hit |
| Slub Whip (`slub_whip`) | 8 | 1 | 0.2 | 15 | 0.5 | 300 | Places bamboo blocks |
| Amethyst Whip (`amethyst_whip`) | 12.5 | 1 | 0.5 | 15 | 0.8 | 700 | — |
| Topaz Whip (`topaz_whip`) | 12.5 | 1 | 0.5 | 15 | 0.8 | 700 | — |
| Jade Whip (`jade_whip`) | 12.6 | 1 | 0.5 | 15 | 0.8 | 900 | — |
| Sapphire Whip (`sapphire_whip`) | 12.6 | 1 | 0.5 | 15 | 0.8 | 720 | — |
| Ruby Whip (`ruby_whip`) | 12.7 | 1 | 0.5 | 15 | 0.8 | 760 | — |
| Amber Whip (`amber_whip`) | 12.7 | 1 | 0.5 | 15 | 0.8 | 740 | — |
| Diamond Whip (`diamond_whip`) | 12.8 | 1 | 0.5 | 15 | 0.8 | 1000 | — |
| Swamp Whip (`swamp_whip`) | 16 | 2 | 0.6 | 15 | 1.6 | 1200 | Slowness, leaf particles |
| Snapthorn (`snapthorn`) | 18 | 3 | 0.7 | 15 | 1.85 | 3600 | Poison II, attack speed boost |
| Spinal Tap (`spinal_tap`) | 29 | 4 | 0.8 | 13 | 1.6 | 3600 | — |
| Firecracker (`firecracker`) | 37 | 0 | 0.5 | 15 | 1.85 | 3600 | Hellfire effect |

**Mechanics:**
- Left-click to swing; whip extends in an arc and retracts
- **Tag system:** hit enemies are "tagged" — your minions prioritize them and deal bonus mark damage
- Whip range scales with `WHIP_RANGE` attribute (from ConfluenceMagicLib)
- Attack speed modifier affects swing speed
- Status effects on hit (strength, slowness, poison, hellfire)
- Some whips produce particles (leaves, etc.)
