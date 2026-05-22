> **AI-generated** — 2026-05-22

# Summoning System (召唤系统)

The summoning system allows players to summon minions and sentries to aid in combat. It uses a **capacity-based** system with separate pools for minions and sentries.

---

## Minion Capacity

Each player has a base minion capacity determined by the `MINION_CAPACITY` attribute (from ConfluenceMagicLib). Capacity can be increased by:

- **Summoner's Pact** enchantment on helmets: +1 per level (+0.5 on fractional levels)
- Equipment or potion effects that modify `MINION_CAPACITY`

### How It Works
1. Start with `currentCapacity = maxCapacity`
2. Summoning a minion: `currentCapacity -= cost` (cost is typically 1)
3. Minion dies/removed: `currentCapacity += cost`
4. `canSummon(cost)` returns false if insufficient capacity
5. Insufficient slots → oldest non-pet summon is discarded to free a slot

---

## Summoning Flow

1. **Right-click + hold** with a summon staff
2. After 20 ticks of use, all current summons are cleared
3. On release (if held < 20 ticks):
   - Check `canSummon(cost)`
   - If capacity insufficient, `removeLast()` frees a slot
   - Entity is created at eye-line-of-sight position
   - Entity ID is registered in `SummonerAttachment`
   - Capacity is synced to client

### On Player Events
- **Death / Respawn:** All summons cleared, capacity resets
- **Level leave:** All summons cleared
- **Owner dies while summoned:** Summons auto-discard (`summon_discardWhenOwnerDie`)

---

## Damage Calculation

Minion damage is calculated as:

```
finalDamage = baseDamage × (1 + owner.summon_damage) + owner.mark_damage
```

- `baseDamage` — Staff's base damage
- `summon_damage` — Player's `SUMMON_DAMAGE` attribute multiplier
- `mark_damage` — Flat bonus from whip tagging (`MARK_DAMAGE` attribute)

Damage type: `SUMMONER` (bypasses some immunities, no knockback).

---

## Summon Keys

Each summon staff has a **Summon Key** that determines slot priority (lower = higher priority):

| Key | Staff |
|-----|-------|
| 1 | Stardust Dragon Staff |
| 2 | Finch Staff, Wooden Sword Staff |
| 3 | Stone Sword Staff |
| 4 | Iron Sword Staff |
| 5 | Slime Staff, Golden Sword Staff |
| 6 | Diamond Sword Staff |
| 7 | Sculk Wisp Staff, Snow Flinx Staff, Netherite Sword Staff |
| 8 | Hornet Staff, Iron Golem Staff |
| 14 | Imp Staff |
| 18 | Terraprisma |

---

## Summon Types

### Minions
Autonomous creatures that follow the player and attack enemies. One per staff use, consume 1 minion slot.

### Sword Staffs (棱镜系列)
Swords that orbit behind the player in sequence formation. They perform:
- **Auto-attack:** Fly toward target and deal contact damage
- **Slash Skill:** Cooldown-based → fly above target, slash down with +30% damage
- **Sequence positioning:** Each sword has a numbered position behind the player

### Stardust Dragon
Unique segmented worm summon. Reusing the staff on an existing dragon **adds a segment** instead of creating a new one (infinite merge). Each segment is a separate entity part.

### Pets
Pets (Chester, Piggy Bank) cost **0 minion slots** and do not consume capacity. They provide utility functions (storage access).

### Sentries
Separate capacity pool from minions (`SENTRY_STORAGE`). Currently not implemented with items.

---

## Whip & Tag System

Whips are summoner weapons that apply a **summon tag** to hit enemies.

### Tag Mechanics
1. Hit an enemy with a whip → enemy is **tagged**
2. All your minions prioritize tagged targets
3. Minions deal **bonus mark damage** (`MARK_DAMAGE` attribute) on tagged targets

### Summon Focus Effect
- Applied by whip attacks or special abilities
- Increases summon damage by +2 flat against the affected target
- Stacks with mark damage bonus

### Whip Stats
- **Damage:** Base summon damage
- **Mark Damage:** Bonus damage applied through minions on tagged targets
- **Attack Speed:** Modifies swing speed
- **Cooldown:** Ticks between hits on same target
- **Range Factor:** Scales with `WHIP_RANGE` attribute

See [Whips](https://github.com/Magic-team-jvav/TerraEntity/wiki/Items-Weapons-Whips) for individual whip stats.

---

## Curios Integration

- **Pet slot:** Chester, Piggy Bank can be placed in Curios pet slot
- **Mount slot:** Slimy Saddle, Honeyed Goggles can be placed in Curios mount slot
- Auto-summon when placed in slot, auto-dismiss when removed

---

## Configuration

All summon damage attributes are defined in ConfluenceMagicLib:
- `MINION_CAPACITY` — Base minion slots
- `SENTRY_CAPACITY` — Base sentry slots
- `SUMMON_DAMAGE` — Damage multiplier
- `MARK_DAMAGE` — Whip tag flat bonus
- `SUMMON_KNOCKBACK` — Knockback modifier
