> **AI-generated** — 2026-05-22

# Yoyos (悠悠球)

Yoyos are melee-range throwing weapons. They extend from the player on a string, orbit around or follow the cursor, and return when the player stops attacking. Each yoyo has a maximum range and a string color.

- **Damage:** per hit
- **Range:** max distance in blocks before automatic return
- **Tag Length:** length of the yoyo string
- **Rarity:** White → Blue → Green → Orange

| Name | Damage | Range | Tag Length | Durability | Rarity | Color | Special Effect |
|------|--------|-------|-----------|------------|--------|-------|---------------|
| Wooden Yoyo (`wooden_yoyo`) | 1.5 | 8 | 3 | 150 | White | Green | — |
| Rally (`rally`) | 3.5 | 10 | 5 | 350 | Blue | Blue | — |
| Malaise (`malaise`) | 3.8 | 12 | 7 | 380 | Blue | Blue | — |
| Artery (`artery`) | 4.2 | 13 | 6 | 420 | Blue | Blue | — |
| Amazon (`amazon`) | 4.5 | 14 | 8 | 450 | Orange | Yellow | — |
| Code 1 (`code_1`) | 4.8 | 14 | 9 | 480 | Green | Green | — |
| Hive Five (`hive_five`) | 5.2 | 14 | 8 | 520 | Orange | Yellow | Spawns bees on hit |
| Cascade (`cascade`) | 5.5 | 15 | 13 | 550 | Orange | Yellow | Sets target on fire |
| Valor (`valor`) | 5.7 | 15 | 11 | 570 | Orange | Yellow | — |

**Mechanics:**
- Left-click to throw; stops when releasing or reaching max range
- Yoyo returns along the string
- Some yoyos apply status effects (fire, bees) via `IEffectStrategy`
- String rendered with configurable color
