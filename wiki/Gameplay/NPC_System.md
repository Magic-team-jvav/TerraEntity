> **AI-generated** — 2026-05-22

# NPC System (NPC 系统)

TerraEntity adds a town NPC system with housing, trading, mood, and combat AI. 21 NPC types are available.

---

## NPC List

| NPC | Type | Special AI |
|-----|------|-----------|
| Guide | SimpleNPC | Progression tasks |
| Demolitionist | SimpleNPC | Throws TNT (terrain-safe explosion) |
| Arms Dealer | SimpleNPC | Crossbow ranged attack (range 13) |
| Nurse | SimpleNPC | Heals nearby allies with thrown potions |
| Merchant | SimpleNPC | Basic trades |
| Painter | SimpleNPC | Dye trades |
| Dryad | SimpleNPC | Sapling trades |
| Dye Trader | SimpleNPC | Dye trades |
| Angler | AnglerNPC | Sleeps at night, daily fishing quests |
| Female Angler | AnglerNPC | Same as Angler |
| Old Man | OldManNPC | Passive, no attack/panic/house AI |
| Mechanic | MechanicNPC | Redstone trades, despawns when far |
| Traveling Merchant | TravelingMerchantNPC | Despawns at night, weighted random trades |
| Goblin Tinkerer | SimpleNPC | — |
| Witch Doctor | SimpleNPC | Honey/potion trades |
| Party Girl | SimpleNPC | Fireworks trades |
| Clothier | SimpleNPC | Iron armor trades |
| Zoologist | SimpleNPC | Pet/mount trades |
| Truffle | SimpleNPC | Mushroom trades |
| Wizard | SimpleNPC | — |

---

## Housing System

Each NPC needs a **house** (detected room) to live in. Use the **House Detector** tool to manage housing.

### House Detection Algorithm
- **Range:** 20 blocks flood-fill from target block
- **Requirements:**
  - Size ≥ 16 blocks
  - XZ span ≥ 4 blocks
  - Light source (> 10 light emission)
  - Chair block (tagged `npc_house_chair`)
  - Table block (tagged `npc_house_table`)
- **Error types:** `TOO_LARGE`, `TOO_SMALL`, `NO_DYNAMIC_LIGHT`, `NO_CHAIR`, `NO_TABLE`, `FOUND_HOUSE`

### House Detector Tool Modes
- **CHECK** — Right-click a block to detect house
- **ADD** — Assign detected house to an NPC
- **DELETE** — Remove NPC's house assignment

### Behavior
- NPCs return home at night (teleport if > 500 blocks away)
- Houses are persisted in world save data
- Housing affects NPC availability and mood

---

## Trade System

NPCs offer trades loaded from datapack JSON files at `data/<modid>/npc/shop/<npc_id>.json`.

### Trade Types
- `ITradeItem` — Single item trade
- `ITradeItemList` — Multi-item input/output
- `ITradeHealth` — Health-restoring (Nurse)
- `ITradeLootTable` — Loot-table reward
- `ITradeTask` — Task-based (progression, quests)
- **Trade Lock** — Some trades locked until conditions met (e.g., kill a boss first)

### Trade Modifiers
- `ADD` — Add trades to existing list
- `DEL` — Remove trades
- `REPLACE` — Replace trades
- Loaded from `data/<modid>/npc/trade_modifiers/`

---

## Mood System

NPCs have a mood value (base 100) affected by nearby NPCs.

| Mood | Value |
|------|-------|
| LOVER | +20 |
| LIKE | +10 |
| NEUTRAL | 0 |
| DISLIKE | -10 |
| HATE | -20 |

Mood data is loaded from `npc/moods.json` (data-driven). For example: Arms Dealer LOVES Nurse, Demolitionist HATES Arms Dealer.

---

## Combat AI

NPCs defend themselves with unique behaviors:
- **Melee/Ranged:** Most NPCs use ranged attacks
- **Demolitionist:** Places terrain-safe TNT (range 5)
- **Arms Dealer:** Crossbow (range 13, cooldown 30)
- **Nurse:** Throws healing potions at hurt allies (range 5)
- **Panic:** Flees from hostiles; does not fear players/NPCs
- **NPC Talk:** NPCs converse with each other (800-tick interval, 200-tick duration)

## Dialog & Chat

NPCs display timed chat bubbles with emoji, items, and text. Loaded from `npc/chat/<id>.json`. Chat bubble style configurable in Client Config (RECT / CLOUD).

## JEI Integration

NPC trades are displayed in JEI with inputs, outputs, spawn egg icon, and lock conditions.
