> **AI-generated** — 2026-05-22

# NPCs (城镇 NPC)

Town NPCs that provide trades, housing, and unique services. All use `AbstractTerraNPC` with specific brain AI.

---

## NPC List

| NPC | Class | Size | Special AI |
|-----|-------|------|-----------|
| Guide (`guide`) | SimpleNPC | 0.6×1.85 | Progression task trades |
| Demolitionist (`demolitionist`) | SimpleNPC | 0.6×1.85 | Throws TNT (terrain-safe, range 5) |
| Arms Dealer (`arms_dealer`) | SimpleNPC | 0.6×1.85 | Crossbow (range 13, cooldown 30) |
| Nurse (`nurse`) | SimpleNPC | 0.6×1.85 | Heals allies with thrown potions (range 5) |
| Merchant (`merchant`) | SimpleNPC | 0.6×1.85 | Ore and basic item trades |
| Painter (`painter`) | SimpleNPC | 0.6×1.85 | Dye trades |
| Dryad (`dryad`) | SimpleNPC | 0.6×1.85 | Sapling/nature trades |
| Dye Trader (`dye_trader`) | SimpleNPC | 0.6×1.85 | Dye trades |
| Angler (`angler`) | AnglerNPC | 0.6×1.65 | Sleeps at night, daily fishing quests |
| Female Angler (`female_angler`) | AnglerNPC | 0.45×1.45 | Same as Angler |
| Old Man (`old_man`) | SimpleNPC | 0.6×1.85 | Passive, no attack, no house AI |
| Mechanic (`mechanic`) | MechanicNPC | 0.6×1.85 | Redstone trades, despawns when far |
| Goblin Tinkerer (`goblin_tinkerer`) | SimpleNPC | 0.6×1.85 | Reroll/upgrade trades |
| Witch Doctor (`witch_doctor`) | SimpleNPC | 0.6×1.85 | Honey/potion trades |
| Party Girl (`party_girl`) | SimpleNPC | 0.6×1.85 | Fireworks trades |
| Clothier (`clothier`) | SimpleNPC | 0.6×1.85 | Armor trades |
| Zoologist (`zoologist`) | SimpleNPC | 0.6×1.85 | Pet/mount trades |
| Truffle (`truffle`) | SimpleNPC | 0.6×1.85 | Mushroom trades |
| Wizard (`wizard`) | SimpleNPC | 0.6×1.85 | Magic item trades |
| Traveling Merchant (`traveling_merchant`) | TravelingMerchantNPC | 0.6×1.85 | Despawns at night, weighted random trades |

---

## Core Mechanics

- **Housing:** NPCs need a house (see [NPC System](https://github.com/Magic-team-jvav/TerraEntity/wiki/Gameplay-NPC_System))
- **Trading:** Right-click to open trade GUI. Trades are data-driven via `npc/shop/<id>.json`
- **Combat:** Most NPCs defend themselves with ranged attacks
- **Mood:** NPC happiness is affected by nearby NPC types (see mood system)
- **Dialog:** Timed chat bubbles with emoji, items, and text
- **NPC Names:** Randomly generated from weighted name lists (`npc/names.json`)

## Unique Behaviors

- **Guide:** Progression-based task trades with kill locks (boss summons)
- **Demolitionist:** TNT does not destroy terrain (except portals), only damages enemies
- **Nurse:** Throws Healing potions, does not attack enemies; panics instead
- **Angler:** Sleeps at night (lies down), daily fishing quests with progressive rewards (up to 55 tiers)
- **Traveling Merchant:** Despawns at night; trades are weighted random selections
- **Old Man:** Fully passive — no attack, no panic, no housing AI
- **Mechanic:** Despawns when far from player (unlike other NPCs)
- **NPC Talk:** NPCs converse with each other (800-tick check, 200-tick conversation)
